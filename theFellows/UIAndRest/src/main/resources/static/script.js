// Global variables
let trafficChart, anomalyPieChart, topNodesChart, nodeTrafficChart;
let trafficData = [];
let anomaliesData = [];
let nodesList = [];
let networksList = [];

// Document ready - updated to ensure proper initialization
$(document).ready(function() {
    // Initialize DataTables AFTER ensuring jQuery and DataTables are loaded
    if ($.fn.DataTable) {
        const trafficTable = $('#trafficTable').DataTable({
            responsive: true,
            order: [[1, 'desc']],
            initComplete: function() {
                console.log('Traffic DataTable initialized');
            }
        });

        const anomaliesTable = $('#anomaliesTable').DataTable({
            responsive: true,
            order: [[1, 'desc']],
            initComplete: function() {
                console.log('Anomalies DataTable initialized');
            }
        });
    } else {
        console.error('DataTables not loaded!');
    }

    // Initialize charts
    initCharts();

    // Load initial data
    loadAllData();

    // Event listeners
    $('#applyFilters').click(applyFilters);
    $('#refreshData').click(loadAllData);
    $('#selectedNode').change(updateNodeAnalysis);

    // Modal events
    $('#trafficDetailsModal').on('show.bs.modal', function(event) {
        const button = $(event.relatedTarget);
        const trafficId = button.data('id');
        loadTrafficDetails(trafficId);
    });

    $('#anomalyDetailsModal').on('show.bs.modal', function(event) {
        const button = $(event.relatedTarget);
        const anomalyId = button.data('id');
        loadAnomalyDetails(anomalyId);
    });
});

// Initialize charts
function initCharts() {
    // Traffic Volume Over Time Chart
    const trafficCtx = document.getElementById('trafficChart').getContext('2d');
    trafficChart = new Chart(trafficCtx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Traffic Volume',
                data: [],
                backgroundColor: 'rgba(78, 115, 223, 0.05)',
                borderColor: 'rgba(78, 115, 223, 1)',
                pointBackgroundColor: 'rgba(78, 115, 223, 1)',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: 'rgba(78, 115, 223, 1)',
                borderWidth: 2,
                tension: 0.3,
                fill: true
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Traffic Volume'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Time'
                    }
                }
            },
            plugins: {
                tooltip: {
                    mode: 'index',
                    intersect: false
                },
                legend: {
                    display: false
                }
            },
            interaction: {
                intersect: false,
                mode: 'nearest'
            }
        }
    });

    // Anomaly Pie Chart
    const anomalyPieCtx = document.getElementById('anomalyPieChart').getContext('2d');
    anomalyPieChart = new Chart(anomalyPieCtx, {
        type: 'doughnut',
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: [
                    'rgba(231, 74, 59, 0.8)',
                    'rgba(54, 185, 204, 0.8)',
                    'rgba(246, 194, 62, 0.8)',
                    'rgba(133, 135, 150, 0.8)'
                ],
                hoverBackgroundColor: [
                    'rgba(231, 74, 59, 1)',
                    'rgba(54, 185, 204, 1)',
                    'rgba(246, 194, 62, 1)',
                    'rgba(133, 135, 150, 1)'
                ],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }]
        },
        options: {
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `${context.label}: ${context.raw} (${context.formattedValue})`;
                        }
                    }
                }
            },
            cutout: '70%'
        }
    });

    // Top Nodes Chart
    const topNodesCtx = document.getElementById('topNodesChart').getContext('2d');
    topNodesChart = new Chart(topNodesCtx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: 'Traffic Volume',
                data: [],
                backgroundColor: 'rgba(78, 115, 223, 0.8)',
                hoverBackgroundColor: 'rgba(78, 115, 223, 1)',
                borderColor: "",
                borderWidth: 1
            }]
        },
        options: {
            maintainAspectRatio: false,
            indexAxis: 'y',
            scales: {
                x: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Traffic Volume'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Node ID'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });

    // Node Traffic Chart
    const nodeTrafficCtx = document.getElementById('nodeTrafficChart').getContext('2d');
    nodeTrafficChart = new Chart(nodeTrafficCtx, {
        type: 'line',
        data: {
            labels: [],
            datasets: [{
                label: 'Traffic Volume',
                data: [],
                backgroundColor: 'rgba(28, 200, 138, 0.05)',
                borderColor: 'rgba(28, 200, 138, 1)',
                pointBackgroundColor: 'rgba(28, 200, 138, 1)',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: 'rgba(28, 200, 138, 1)',
                borderWidth: 2,
                tension: 0.3,
                fill: true
            }]
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Traffic Volume'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Time'
                    }
                }
            },
            plugins: {
                tooltip: {
                    mode: 'index',
                    intersect: false
                },
                legend: {
                    display: false
                }
            },
            interaction: {
                intersect: false,
                mode: 'nearest'
            }
        }
    });
}

// Load all data
function loadAllData() {
    showLoading();
    Promise.all([
        fetch('http://localhost:9095/api/traffic').then(res => res.json()),
        fetch('http://localhost:9095/api/anomalies').then(res => res.json())
    ])
    .then(([traffic, anomalies]) => {
        trafficData = traffic;
        anomaliesData = anomalies;

        // Extract unique nodes and networks
        nodesList = [...new Set(trafficData.map(item => item.nodeId))].sort((a, b) => a - b);
        networksList = [...new Set(trafficData.map(item => item.networkId))].sort((a, b) => a - b);

        // Update filters
        updateFilters();

        // Update dashboard
        updateDashboard();

        // Update tables
        updateTables();

        hideLoading();
    })
    .catch(error => {
        console.error('Error loading data:', error);
        hideLoading();
        alert('Error loading data. Please check console for details.');
    });
}

// Update filters dropdowns
function updateFilters() {
    // Network filter
    const networkFilter = $('#networkFilter');
    networkFilter.empty();
    networkFilter.append('<option value="all">All Networks</option>');
    networksList.forEach(networkId => {
        networkFilter.append(`<option value="${networkId}">Network ${networkId}</option>`);
    });

    // Node filter
    const nodeFilter = $('#nodeFilter');
    nodeFilter.empty();
    nodeFilter.append('<option value="all">All Nodes</option>');
    nodesList.forEach(nodeId => {
        nodeFilter.append(`<option value="${nodeId}">Node ${nodeId}</option>`);
    });

    // Node analysis dropdown
    const selectedNode = $('#selectedNode');
    selectedNode.empty();
    nodesList.forEach(nodeId => {
        selectedNode.append(`<option value="${nodeId}">Node ${nodeId}</option>`);
    });
}

// Apply filters
function applyFilters() {
    const timeRange = $('#timeRange').val();
    const networkId = $('#networkFilter').val();
    const nodeId = $('#nodeFilter').val();

    let filteredTraffic = [...trafficData];
    let filteredAnomalies = [...anomaliesData];

    // Apply time filter (assuming timestamp is in ISO format)
    const now = new Date();
    let timeThreshold = new Date();

    if (timeRange === '1') {
        timeThreshold.setHours(now.getHours() - 1);
    } else if (timeRange === '6') {
        timeThreshold.setHours(now.getHours() - 6);
    } else if (timeRange === '24') {
        timeThreshold.setDate(now.getDate() - 1);
    } else if (timeRange === '168') {
        timeThreshold.setDate(now.getDate() - 7);
    }

    filteredTraffic = filteredTraffic.filter(item => new Date(item.timestamp) >= timeThreshold);
    filteredAnomalies = filteredAnomalies.filter(item => new Date(item.timestamp) >= timeThreshold);

    // Apply network filter
    if (networkId !== 'all') {
        filteredTraffic = filteredTraffic.filter(item => item.networkId == networkId);
        filteredAnomalies = filteredAnomalies.filter(item => item.networkId == networkId);
    }

    // Apply node filter
    if (nodeId !== 'all') {
        filteredTraffic = filteredTraffic.filter(item => item.nodeId == nodeId);
        filteredAnomalies = filteredAnomalies.filter(item => item.nodeId == nodeId);
    }

    // Update charts and tables with filtered data
    updateCharts(filteredTraffic, filteredAnomalies);
    updateTables(filteredTraffic, filteredAnomalies);
}

// Update dashboard with filtered data
function updateDashboard() {
    // Calculate metrics
    const totalTraffic = trafficData.reduce((sum, item) => sum + item.trafficVolume, 0);
    const activeNodes = new Set(trafficData.map(item => item.nodeId)).size;
    const totalAnomalies = anomaliesData.length;
    const criticalAlerts = anomaliesData.filter(anomaly =>
        anomaly.anomalyType === 'traffic_spike' || anomaly.anomalyType === 'ddos_attack'
    ).length;

    // Update metric cards
    $('#totalTraffic').text(totalTraffic.toLocaleString());
    $('#activeNodes').text(activeNodes);
    $('#totalAnomalies').text(totalAnomalies);
    $('#criticalAlerts').text(criticalAlerts);

    // Update charts
    updateCharts(trafficData, anomaliesData);

    // Update recent anomalies table
    const recentAnomalies = anomaliesData
        .sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
        .slice(0, 5);

    const recentAnomaliesTable = $('#recentAnomaliesTable tbody');
    recentAnomaliesTable.empty();

    recentAnomalies.forEach(anomaly => {
        const time = new Date(anomaly.timestamp).toLocaleTimeString();
        const anomalyClass = getAnomalyClass(anomaly.anomalyType);

        recentAnomaliesTable.append(`
            <tr>
                <td>${time}</td>
                <td>Node ${anomaly.nodeId}</td>
                <td><span class="badge ${anomalyClass}">${formatAnomalyType(anomaly.anomalyType)}</span></td>
                <td>${anomaly.trafficVolume.toLocaleString()}</td>
            </tr>
        `);
    });
}

// Update charts with filtered data
function updateCharts(trafficData, anomaliesData) {
    // Traffic Volume Over Time Chart
    const timeLabels = trafficData
        .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp))
        .map(item => new Date(item.timestamp).toLocaleTimeString());

    const trafficVolumes = trafficData
        .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp))
        .map(item => item.trafficVolume);

    trafficChart.data.labels = timeLabels;
    trafficChart.data.datasets[0].data = trafficVolumes;
    trafficChart.update();

    // Anomaly Pie Chart
    const anomalyTypes = {};
    anomaliesData.forEach(anomaly => {
        const type = formatAnomalyType(anomaly.anomalyType);
        anomalyTypes[type] = (anomalyTypes[type] || 0) + 1;
    });

    anomalyPieChart.data.labels = Object.keys(anomalyTypes);
    anomalyPieChart.data.datasets[0].data = Object.values(anomalyTypes);
    anomalyPieChart.update();

    // Top Nodes Chart
    const nodeTraffic = {};
    trafficData.forEach(item => {
        nodeTraffic[item.nodeId] = (nodeTraffic[item.nodeId] || 0) + item.trafficVolume;
    });

    const sortedNodes = Object.entries(nodeTraffic)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 5);

    topNodesChart.data.labels = sortedNodes.map(item => `Node ${item[0]}`);
    topNodesChart.data.datasets[0].data = sortedNodes.map(item => item[1]);
    topNodesChart.update();
}

// Update tables with filtered data
function updateTables(trafficData = [], anomaliesData = []) {
    // Traffic Data Table
    const trafficTable = $('#trafficTable').DataTable();
    trafficTable.clear();

    trafficData.forEach(item => {
        trafficTable.row.add([
            item.id,
            new Date(item.timestamp).toLocaleString(),
            item.nodeId,
            item.networkId,
            item.trafficVolume.toLocaleString(),
            `<button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#trafficDetailsModal" data-id="${item.id}">
                <i class="fas fa-search"></i>
            </button>`
        ]);
    });

    trafficTable.draw();

    // Anomalies Table
    const anomaliesTable = $('#anomaliesTable').DataTable();
    anomaliesTable.clear();

    anomaliesData.forEach(item => {
        const anomalyClass = getAnomalyClass(item.anomalyType);

        anomaliesTable.row.add([
            item.id,
            new Date(item.timestamp).toLocaleString(),
            item.nodeId,
            item.networkId,
            `<span class="badge ${anomalyClass}">${formatAnomalyType(item.anomalyType)}</span>`,
            item.trafficVolume.toLocaleString(),
            `<button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#anomalyDetailsModal" data-id="${item.id}">
                <i class="fas fa-search"></i>
            </button>`
        ]);
    });

    anomaliesTable.draw();
}

// Update node analysis
function updateNodeAnalysis() {
    const nodeId = $('#selectedNode').val();
    if (!nodeId) return;

    // Filter traffic for this node
    const nodeTraffic = trafficData
        .filter(item => item.nodeId == nodeId)
        .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

    // Update chart
    nodeTrafficChart.data.labels = nodeTraffic.map(item => new Date(item.timestamp).toLocaleTimeString());
    nodeTrafficChart.data.datasets[0].data = nodeTraffic.map(item => item.trafficVolume);
    nodeTrafficChart.update();

    // Update node details
    const nodeAnomalies = anomaliesData.filter(item => item.nodeId == nodeId);
    const nodeNetwork = nodeTraffic.length > 0 ? nodeTraffic[0].networkId : 'N/A';

    const detailsHtml = `
        <div class="row">
            <div class="col-md-6">
                <h5>Node ${nodeId}</h5>
                <p class="text-muted">Network: ${nodeNetwork}</p>
                <hr>
                <p><i class="fas fa-chart-line me-2"></i>Total Traffic: ${nodeTraffic.reduce((sum, item) => sum + item.trafficVolume, 0).toLocaleString()}</p>
                <p><i class="fas fa-exclamation-triangle me-2"></i>Anomalies Detected: ${nodeAnomalies.length}</p>
            </div>
            <div class="col-md-6">
                <h5>Recent Activity</h5>
                <ul class="list-group">
                    ${nodeAnomalies.slice(0, 3).map(anomaly => `
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            ${formatAnomalyType(anomaly.anomalyType)}
                            <span class="badge bg-primary rounded-pill">${new Date(anomaly.timestamp).toLocaleTimeString()}</span>
                        </li>
                    `).join('')}
                    ${nodeAnomalies.length === 0 ? '<li class="list-group-item text-muted">No recent anomalies</li>' : ''}
                </ul>
            </div>
        </div>
    `;

    $('#nodeDetails').html(detailsHtml);
}

// Load traffic details for modal
function loadTrafficDetails(id) {
    fetch(`http://localhost:9095/api/traffic/${id}`)
        .then(res => res.json())
        .then(data => {
            const detailsHtml = `
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>ID:</strong> ${data.id}</p>
                        <p><strong>Timestamp:</strong> ${new Date(data.timestamp).toLocaleString()}</p>
                        <p><strong>Node ID:</strong> ${data.nodeId}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Network ID:</strong> ${data.networkId}</p>
                        <p><strong>Traffic Volume:</strong> ${data.trafficVolume.toLocaleString()}</p>
                    </div>
                </div>
                <div class="mt-3">
                    <h5>Traffic Pattern</h5>
                    <canvas id="trafficDetailsChart" height="200"></canvas>
                </div>
            `;

            $('#trafficDetailsContent').html(detailsHtml);

            // Create mini chart for details
            const ctx = document.getElementById('trafficDetailsChart').getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: ['-2h', '-1h', 'Current', '+1h', '+2h'],
                    datasets: [{
                        label: 'Traffic Volume',
                        data: [
                            data.trafficVolume * 0.7,
                            data.trafficVolume * 0.9,
                            data.trafficVolume,
                            data.trafficVolume * 1.1,
                            data.trafficVolume * 0.8
                        ],
                        borderColor: 'rgba(78, 115, 223, 1)',
                        tension: 0.1,
                        fill: false
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error loading traffic details:', error);
            $('#trafficDetailsContent').html('<p class="text-danger">Error loading details. Please try again.</p>');
        });
}

// Load anomaly details for modal
function loadAnomalyDetails(id) {
    fetch(`http://localhost:9095/api/anomalies/${id}`)
        .then(res => res.json())
        .then(data => {
            const anomalyClass = getAnomalyClass(data.anomalyType);

            const detailsHtml = `
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>ID:</strong> ${data.id}</p>
                        <p><strong>Timestamp:</strong> ${new Date(data.timestamp).toLocaleString()}</p>
                        <p><strong>Node ID:</strong> ${data.nodeId}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Network ID:</strong> ${data.networkId}</p>
                        <p><strong>Type:</strong> <span class="badge ${anomalyClass}">${formatAnomalyType(data.anomalyType)}</span></p>
                        <p><strong>Traffic Volume:</strong> ${data.trafficVolume.toLocaleString()}</p>
                    </div>
                </div>
                <div class="mt-3">
                    <h5>Anomaly Context</h5>
                    <canvas id="anomalyDetailsChart" height="200"></canvas>
                </div>
                <div class="mt-3">
                    <h5>Suggested Actions</h5>
                    <ul class="list-group">
                        ${getSuggestedActions(data.anomalyType).map(action => `
                            <li class="list-group-item">${action}</li>
                        `).join('')}
                    </ul>
                </div>
            `;

            $('#anomalyDetailsContent').html(detailsHtml);

            // Create mini chart for details
            const ctx = document.getElementById('anomalyDetailsChart').getContext('2d');
            new Chart(ctx, {
                type: 'line',
                data: {
                    labels: ['-2h', '-1h', 'Anomaly', '+1h', '+2h'],
                    datasets: [{
                        label: 'Traffic Volume',
                        data: [
                            data.trafficVolume * 0.5,
                            data.trafficVolume * 0.7,
                            data.trafficVolume,
                            data.trafficVolume * 0.6,
                            data.trafficVolume * 0.4
                        ],
                        borderColor: 'rgba(231, 74, 59, 1)',
                        tension: 0.1,
                        fill: false
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            display: false
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: false
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error loading anomaly details:', error);
            $('#anomalyDetailsContent').html('<p class="text-danger">Error loading details. Please try again.</p>');
        });
}

// Helper functions
function formatAnomalyType(type) {
    return type.split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
}

function getAnomalyClass(type) {
    if (type.includes('spike')) return 'badge-spike';
    if (type.includes('dip')) return 'badge-dip';
    if (type.includes('flood')) return 'badge-flood';
    return 'badge-other';
}

function getSuggestedActions(type) {
    if (type.includes('spike')) {
        return [
            "Check for DDoS attacks on Node",
            "Verify if this is expected traffic (e.g., scheduled backup)",
            "Consider rate limiting if pattern continues",
            "Review firewall rules for this node"
        ];
    } else if (type.includes('dip')) {
        return [
            "Check node connectivity and health",
            "Verify if maintenance was scheduled",
            "Review routing tables for this node",
            "Check for hardware failures"
        ];
    } else if (type.includes('flood')) {
        return [
            "Investigate possible broadcast storm",
            "Check spanning tree configuration",
            "Verify VLAN configurations",
            "Inspect for possible loops in network"
        ];
    } else {
        return [
            "Review node logs for unusual activity",
            "Check for configuration changes",
            "Compare with baseline traffic patterns",
            "Monitor for recurrence of this anomaly"
        ];
    }
}

function showLoading() {
    // You could implement a loading spinner here
    console.log('Loading data...');
}

function hideLoading() {
    console.log('Data loaded');
}