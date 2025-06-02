// Global variables
let trafficChart, anomalyPieChart, topNodesChart;
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
}

// Load all data
function loadAllData() {
    Promise.all([
        fetch('/api/traffic').then(res => res.json()),
        fetch('/api/anomalies').then(res => res.json())
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

    })
    .catch(error => {
        console.error('Error loading data:', error);
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
        anomaly.anomalyType === 'Sudden Spike' || anomaly.anomalyType === 'Sudden Drop'
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
                <td><span class="badge ${anomalyClass}">${anomaly.anomalyType}</span></td>
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
        const type = anomaly.anomalyType;
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
            item.trafficVolume.toLocaleString()
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
            `<span class="badge ${anomalyClass}">${item.anomalyType}</span>`,
            item.trafficVolume.toLocaleString(),
            `<button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#anomalyDetailsModal" data-id="${item.id}">
                <i class="fas fa-search"></i>
            </button>`
        ]);
    });

    anomaliesTable.draw();
}



// Load anomaly details for modal
function loadAnomalyDetails(id) {
    fetch(`/api/anomalies/${id}`)
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
                        <p><strong>Type:</strong> <span class="badge ${anomalyClass}">${data.anomalyType}</span></p>
                        <p><strong>Traffic Volume:</strong> ${data.trafficVolume.toLocaleString()}</p>
                    </div>
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

        })
        .catch(error => {
            console.error('Error loading anomaly details:', error);
            $('#anomalyDetailsContent').html('<p class="text-danger">Error loading details. Please try again.</p>');
        });
}


function getAnomalyClass(type) {
    const lowerType = type.toLowerCase();

    if (lowerType.includes('spike')) return 'badge-spike';
    if (lowerType.includes('drop')) return 'badge-drop';
    if (lowerType.includes('zero')) return 'badge-zero';
    if (lowerType.includes('high')) return 'badge-high';
    if (lowerType.includes('unusual')) return 'badge-unusual';

    return 'badge-other';
}


function getSuggestedActions(type) {
    if (type.includes('spike')) {
        return [
            "Check for DDoS attacks on Node",
            "Verify if this is expected traffic (e.g., scheduled backup or software update)",
            "Monitor traffic patterns over time",
            "Review firewall and intrusion detection system logs"
        ];
    } else if (type.includes('Drop')) {
        return [
            "Inspect network interface errors or collisions",
            "Verify routing paths between nodes",
            "Check for configuration mismatches or protocol errors",
            "Run connectivity diagnostics (e.g., ping, traceroute)"
        ];
    } else if (type.includes('Zero')) {
        return [
            "Ensure the node is powered on and reachable",
            "Verify monitoring tool is correctly configured",
            "Check if the node was recently removed or decommissioned",
            "Inspect related system and application logs"
        ];
    } else if (type.includes('High')) {
        return [
            "Identify applications generating excess traffic",
            "Check QoS (Quality of Service) policies and limits",
            "Validate performance of connected services",
            "Monitor CPU and memory usage for spikes"
        ];
    } else if (type.includes('Unusual')) {
        return [
            "Compare with normal behavioral baselines",
            "Run packet analysis to identify unexpected patterns",
            "Audit recent configuration or policy changes",
            "Investigate external factors or new device connections"
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
