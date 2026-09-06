import { useEffect, useState } from "react";
import "./App.css";

const menuItems = [
  { name: "Dashboard", icon: "⌂" },
  { name: "Connectors", icon: "↔" },
  { name: "Schema Discovery", icon: "◇" },
  { name: "Semantic Mapping", icon: "✦" },
  { name: "Contracts", icon: "▣" },
  { name: "Data Exchange", icon: "⇄" },
  { name: "Audit Logs", icon: "☷" },
];

function App() {
  const [activePage, setActivePage] = useState("Dashboard");

  return (
    <div className="app">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="logo-section">
          <div className="logo">G</div>
          <div>
            <h2>GovMesh</h2>
            <span>Interoperability Layer</span>
          </div>
        </div>

        <nav>
          <p className="menu-title">MAIN MENU</p>

          {menuItems.map((item) => (
            <button
              key={item.name}
              className={`nav-item ${
                activePage === item.name ? "active" : ""
              }`}
              onClick={() => setActivePage(item.name)}
            >
              <span className="nav-icon">{item.icon}</span>
              {item.name}
            </button>
          ))}
        </nav>

        <div className="sidebar-bottom">
          <div className="system-status">
            <span className="status-dot"></span>
            <div>
              <strong>System Online</strong>
              <small>All services operational</small>
            </div>
          </div>

          <div className="user">
            <div className="avatar">M4</div>
            <div>
              <strong>Frontend Admin</strong>
              <small>Administrator</small>
            </div>
          </div>
        </div>
      </aside>

      {/* Main Content */}
      <main className="main-content">
        <header className="topbar">
          <div>
            <p className="breadcrumb">GovMesh / {activePage}</p>
            <h1>{activePage}</h1>
          </div>

          <div className="topbar-right">
            <button className="notification">🔔</button>

            <div className="profile">
              <div className="avatar">M4</div>
              <span>Admin</span>
            </div>
          </div>
        </header>

        {activePage === "Dashboard" ? (
          <Dashboard />
        ) : activePage === "Connectors" ? (
          <Connectors />
        ) : activePage === "Schema Discovery" ? (
          <SchemaDiscovery />
        ) : activePage === "Semantic Mapping" ? (
          <SemanticMapping />
        ) : activePage === "Contracts" ? (
          <Contracts />
        ) : activePage === "Data Exchange" ? (
          <DataExchange />
        ) : activePage === "Audit Logs" ? (
          <AuditLogs />
        ) : (
          <ComingSoon page={activePage} />
        )}
      </main>
    </div>
  );
}

/* =========================
   DASHBOARD
========================= */

function Dashboard() {
  return (
    <div className="dashboard">
      <div className="welcome">
        <div>
          <h2>Government Data Interoperability</h2>

          <p>
            Connect, map, transform and govern data across government
            departments.
          </p>
        </div>

        <button className="primary-button">+ Add Connector</button>
      </div>

      <div className="stats-grid">
        <StatCard
          title="Connected Systems"
          value="12"
          change="+2 this month"
          icon="↔"
        />

        <StatCard
          title="Active Mappings"
          value="28"
          change="+6 this week"
          icon="◇"
        />

        <StatCard
          title="Active Contracts"
          value="16"
          change="+3 this week"
          icon="▣"
        />

        <StatCard
          title="Data Exchanges"
          value="1,284"
          change="+18.4% this week"
          icon="⇄"
        />
      </div>

      <div className="content-grid">
        <section className="panel exchanges">
          <div className="panel-header">
            <div>
              <h3>Recent Data Exchanges</h3>
              <p>Latest cross-department transactions</p>
            </div>

            <button className="view-button">View all</button>
          </div>

          <div className="table">
            <div className="table-row table-heading">
              <span>Transaction</span>
              <span>Source → Target</span>
              <span>Status</span>
            </div>

            <Exchange
              id="TXN-10293"
              route="Employment → Welfare"
              status="Success"
            />

            <Exchange
              id="TXN-10292"
              route="Welfare → Benefits"
              status="Success"
            />

            <Exchange
              id="TXN-10291"
              route="Legacy → Employment"
              status="Processing"
            />

            <Exchange
              id="TXN-10290"
              route="Employment → Welfare"
              status="Success"
            />
          </div>
        </section>

        <section className="panel">
          <div className="panel-header">
            <div>
              <h3>System Health</h3>
              <p>Current service status</p>
            </div>
          </div>

          <div className="health-list">
            <HealthItem name="API Gateway" />
            <HealthItem name="Semantic Mapper" />
            <HealthItem name="Transformation Engine" />
            <HealthItem name="Audit Service" />
          </div>

          <div className="health-summary">
            <span className="large-dot"></span>

            <div>
              <strong>All systems operational</strong>
              <p>Last checked just now</p>
            </div>
          </div>
        </section>
      </div>

      <section className="panel workflow-panel">
        <div className="panel-header">
          <div>
            <h3>GovMesh Interoperability Workflow</h3>
            <p>How data moves between government systems</p>
          </div>
        </div>

        <div className="workflow">
          <WorkflowStep number="1" title="Connect" />
          <div className="arrow">→</div>

          <WorkflowStep number="2" title="Discover" />
          <div className="arrow">→</div>

          <WorkflowStep number="3" title="Map" />
          <div className="arrow">→</div>

          <WorkflowStep number="4" title="Approve" />
          <div className="arrow">→</div>

          <WorkflowStep number="5" title="Transform" />
          <div className="arrow">→</div>

          <WorkflowStep number="6" title="Govern" />
        </div>
      </section>
    </div>
  );
}

/* =========================
   STAT CARD
========================= */

function StatCard({ title, value, change, icon }) {
  return (
    <div className="stat-card">
      <div className="stat-top">
        <span className="stat-icon">{icon}</span>
        <span className="positive">↗</span>
      </div>

      <h2>{value}</h2>
      <p>{title}</p>
      <small>{change}</small>
    </div>
  );
}

/* =========================
   EXCHANGE
========================= */

function Exchange({ id, route, status }) {
  return (
    <div className="table-row">
      <strong>{id}</strong>

      <span>{route}</span>

      <span
        className={`status ${
          status === "Success" ? "success" : "processing"
        }`}
      >
        {status}
      </span>
    </div>
  );
}

/* =========================
   HEALTH
========================= */

function HealthItem({ name }) {
  return (
    <div className="health-item">
      <span>{name}</span>

      <span className="health-status">
        <span className="status-dot"></span>
        Operational
      </span>
    </div>
  );
}

/* =========================
   WORKFLOW
========================= */

function WorkflowStep({ number, title }) {
  return (
    <div className="workflow-step">
      <div className="workflow-number">{number}</div>
      <strong>{title}</strong>
    </div>
  );
}

/* =========================
   CONNECTORS
========================= */

function Connectors() {
  const [connectors, setConnectors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/departments")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }

        return response.json();
      })
      .then((data) => {
        console.log("DEPARTMENTS FROM BACKEND:", data);

        setConnectors(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("DEPARTMENT ERROR:", error);

        setError(error.message);
        setLoading(false);
      });
  }, []);

  return (
    <div className="dashboard">
      <div className="welcome">
        <div>
          <h2>Government System Connectors</h2>

          <p>
            Connect and manage government departments and legacy systems.
          </p>
        </div>

        <button className="primary-button">+ Add Connector</button>
      </div>

      {loading ? (
        <p>Loading connectors...</p>
      ) : error ? (
        <p>Connector Error: {error}</p>
      ) : (
        <div className="connector-grid">
          {connectors.map((connector) => (
            <div className="connector-card" key={connector.id}>
              <div className="connector-header">
                <div className="connector-icon">↔</div>

                <span className="status success">● Connected</span>
              </div>

              <h3>{connector.name}</h3>

              <p className="connector-type">
                {connector.type} API
              </p>

              <div className="connector-endpoint">
                <span>Endpoint</span>

                <strong>{connector.baseUrl}</strong>
              </div>

              <button className="manage-button">
                Manage Connector →
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

/* =========================
   SCHEMA DISCOVERY
========================= */

function SchemaDiscovery() {
  const [schema, setSchema] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/schemas/1")
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }

        return response.json();
      })
      .then((data) => {
        console.log("SCHEMA FROM BACKEND:", data);

        setSchema(data);
        setLoading(false);
      })
      .catch((error) => {
        console.error("SCHEMA ERROR:", error);

        setError(error.message);
        setLoading(false);
      });
  }, []);

  return (
    <div className="dashboard">
      <div className="welcome">
        <div>
          <h2>Schema Discovery</h2>

          <p>
            Discover and inspect data schemas from connected government
            systems.
          </p>
        </div>
      </div>

      {loading ? (
        <p>Loading schema...</p>
      ) : error ? (
        <p>Schema Error: {error}</p>
      ) : schema ? (
        <div className="schema-grid">
          <div className="schema-card">
            <div className="schema-card-header">
              <div>
                <span className="schema-label">SOURCE SYSTEM</span>

                <h3>{schema.department.name}</h3>
              </div>

              <span className="status success">
                ● Discovered
              </span>
            </div>

            <div className="field-list">
  {Object.entries(JSON.parse(schema.fieldsJson)).map(
    ([fieldName, fieldType]) => (
      <div className="field-row" key={fieldName}>
        <span className="field-name">{fieldName}</span>
        <strong className="field-type">{fieldType}</strong>
      </div>
    )
  )}
</div>
          </div>
        </div>
      ) : (
        <p>No schema found.</p>
      )}
    </div>
  );
}

/* =========================
   SEMANTIC MAPPING
========================= */

function SemanticMapping() {
  const [mappings, setMappings] = useState([
    {
      source: "employee_id",
      target: "beneficiaryId",
      confidence: "98%",
      status: "Pending",
    },
    {
      source: "employee_name",
      target: "applicantName",
      confidence: "96%",
      status: "Pending",
    },
    {
      source: "date_of_birth",
      target: "dateOfBirth",
      confidence: "99%",
      status: "Pending",
    },
    {
      source: "address",
      target: "residentialAddress",
      confidence: "94%",
      status: "Pending",
    },
    {
      source: "employment_status",
      target: "benefitStatus",
      confidence: "89%",
      status: "Pending",
    },
  ]);

  const updateStatus = (source, status) => {
    setMappings((current) =>
      current.map((mapping) =>
        mapping.source === source
          ? { ...mapping, status }
          : mapping
      )
    );
  };

  return (
    <div className="dashboard">

      <div className="welcome">
        <div>
          <h2>Semantic Schema Mapping</h2>
          <p>
            AI-assisted mapping of equivalent fields across government
            systems.
          </p>
        </div>

        <button className="primary-button">
          ✦ Run AI Mapping
        </button>
      </div>

      <div className="mapping-summary">
        <div>
          <span>Source System</span>
          <strong>Employment Department</strong>
        </div>

        <div className="mapping-arrow">→</div>

        <div>
          <span>Target System</span>
          <strong>Welfare Department</strong>
        </div>

        <div className="mapping-score">
          <span>Overall Confidence</span>
          <strong>95.2%</strong>
        </div>
      </div>

      <section className="panel">

        <div className="panel-header">
          <div>
            <h3>AI Suggested Mappings</h3>
            <p>
              Review and approve mappings before data exchange.
            </p>
          </div>

          <span className="ai-badge">✦ AI Generated</span>
        </div>

        <div className="mapping-table">

          <div className="mapping-row mapping-heading">
            <span>Source Field</span>
            <span></span>
            <span>Target Field</span>
            <span>Confidence</span>
            <span>Action</span>
          </div>

          {mappings.map((mapping) => (
            <MappingRow
              key={mapping.source}
              source={mapping.source}
              target={mapping.target}
              confidence={mapping.confidence}
              status={mapping.status}
              onApprove={() =>
                updateStatus(mapping.source, "Approved")
              }
              onReject={() =>
                updateStatus(mapping.source, "Rejected")
              }
            />
          ))}

        </div>
      </section>

      <section className="panel mapping-info-panel">
        <div className="mapping-info">
          <div className="info-icon">✓</div>

          <div>
            <h3>Human-in-the-loop approval</h3>
            <p>
              AI suggestions are reviewed by an authorized administrator
              before they become reusable transformation contracts.
            </p>
          </div>
        </div>
      </section>

    </div>
  );
}

/* =========================
   MAPPING ROW
========================= */

function MappingRow({
  source,
  target,
  confidence,
  status,
  onApprove,
  onReject,
}) {
  return (
    <div className="mapping-row">

      <strong>{source}</strong>

      <span className="mapping-line">→</span>

      <strong>{target}</strong>

      <span className="confidence">
        {confidence}
      </span>

      <div className="mapping-actions">

        {status === "Pending" ? (
          <>
            <button
              className="approve-button"
              onClick={onApprove}
            >
              Approve
            </button>

            <button
              className="reject-button"
              onClick={onReject}
            >
              Reject
            </button>
          </>
        ) : (
          <span
            className={
              status === "Approved"
                ? "status success"
                : "status failed"
            }
          >
            ● {status}
          </span>
        )}

      </div>

    </div>
  );
}
/* =========================
   CONTRACTS
========================= */

function Contracts() {
  const [contracts, setContracts] = useState([
    {
      name: "Employment → Welfare",
      source: "Employment Department",
      target: "Welfare Department",
      fields: 5,
      status: "Active",
      version: "v1.2",
    },
    {
      name: "Welfare → Benefits",
      source: "Welfare Department",
      target: "Benefits Department",
      fields: 8,
      status: "Active",
      version: "v1.0",
    },
    {
      name: "Employment → Benefits",
      source: "Employment Department",
      target: "Benefits Department",
      fields: 6,
      status: "Draft",
      version: "v0.9",
    },
  ]);

  const [showForm, setShowForm] = useState(false);

  const [source, setSource] = useState("Employment Department");
  const [target, setTarget] = useState("Welfare Department");
  const [fields, setFields] = useState(5);

  const createContract = () => {
    const newContract = {
      name: `${source} → ${target}`,
      source: source,
      target: target,
      fields: Number(fields),
      status: "Draft",
      version: "v1.0",
    };

    setContracts([...contracts, newContract]);
    setShowForm(false);
  };

  return (
    <div className="dashboard">

      <div className="welcome">
        <div>
          <h2>Transformation Contracts</h2>
          <p>
            Manage approved mappings and reusable data transformation
            rules.
          </p>
        </div>

        <button
          className="primary-button"
          onClick={() => setShowForm(true)}
        >
          + Create Contract
        </button>
      </div>

      {showForm && (
        <section className="panel contract-form">

          <div className="panel-header">
            <div>
              <h3>Create Transformation Contract</h3>
              <p>
                Define a reusable contract between two government systems.
              </p>
            </div>
          </div>

          <div className="form-grid">

            <div>
              <label>Source System</label>
              <select
                value={source}
                onChange={(e) => setSource(e.target.value)}
              >
                <option>Employment Department</option>
                <option>Welfare Department</option>
                <option>Benefits Department</option>
                <option>Legacy Department</option>
              </select>
            </div>

            <div>
              <label>Target System</label>
              <select
                value={target}
                onChange={(e) => setTarget(e.target.value)}
              >
                <option>Welfare Department</option>
                <option>Benefits Department</option>
                <option>Employment Department</option>
                <option>Legacy Department</option>
              </select>
            </div>

            <div>
              <label>Number of Fields</label>
              <input
                type="number"
                min="1"
                value={fields}
                onChange={(e) => setFields(e.target.value)}
              />
            </div>

          </div>

          <div className="form-actions">
            <button
              className="secondary-button"
              onClick={() => setShowForm(false)}
            >
              Cancel
            </button>

            <button
              className="primary-button"
              onClick={createContract}
            >
              Create Contract
            </button>
          </div>

        </section>
      )}

      <div className="contract-summary">

        <div>
          <span>Active Contracts</span>
          <strong>
            {contracts.filter((c) => c.status === "Active").length}
          </strong>
        </div>

        <div>
          <span>Draft Contracts</span>
          <strong>
            {contracts.filter((c) => c.status === "Draft").length}
          </strong>
        </div>

        <div>
          <span>Mappings Covered</span>
          <strong>
            {contracts.reduce((sum, c) => sum + c.fields, 0)}
          </strong>
        </div>

      </div>

      <section className="panel">

        <div className="panel-header">
          <div>
            <h3>Reusable Transformation Contracts</h3>
            <p>
              Approved contracts used during cross-department data exchange.
            </p>
          </div>
        </div>

        <div className="contract-table">

          <div className="contract-row contract-heading">
            <span>Contract</span>
            <span>Data Flow</span>
            <span>Fields</span>
            <span>Version</span>
            <span>Status</span>
          </div>

          {contracts.map((contract, index) => (
            <div className="contract-row" key={index}>

              <strong>{contract.name}</strong>

              <span>
                {contract.source} → {contract.target}
              </span>

              <span>{contract.fields}</span>

              <span className="version-badge">
                {contract.version}
              </span>

              <span
                className={
                  contract.status === "Active"
                    ? "status success"
                    : "status draft"
                }
              >
                ● {contract.status}
              </span>

            </div>
          ))}

        </div>

      </section>

    </div>
  );
}
/* =========================
   DATA EXCHANGE
========================= */

function DataExchange() {
  const [exchanges, setExchanges] = useState([
    {
      id: "EX-10284",
      flow: "Employment → Welfare",
      records: 124,
      status: "Completed",
      time: "2 min ago",
    },
    {
      id: "EX-10283",
      flow: "Welfare → Benefits",
      records: 86,
      status: "Completed",
      time: "8 min ago",
    },
    {
      id: "EX-10282",
      flow: "Employment → Benefits",
      records: 52,
      status: "Processing",
      time: "12 min ago",
    },
    {
      id: "EX-10281",
      flow: "Benefits → Welfare",
      records: 37,
      status: "Failed",
      time: "18 min ago",
    },
  ]);

  const [showForm, setShowForm] = useState(false);
  const [source, setSource] = useState("Employment");
  const [target, setTarget] = useState("Welfare");
  const [records, setRecords] = useState(10);

  const createExchange = () => {
    const newExchange = {
      id: `EX-${10285 + exchanges.length}`,
      flow: `${source} → ${target}`,
      records: Number(records),
      status: "Processing",
      time: "Just now",
    };

    setExchanges([newExchange, ...exchanges]);
    setShowForm(false);
  };

  return (
    <div className="dashboard">

      <div className="welcome">
        <div>
          <h2>Data Exchange</h2>
          <p>
            Monitor and manage cross-department data exchanges.
          </p>
        </div>

        <button
          className="primary-button"
          onClick={() => setShowForm(true)}
        >
          + New Exchange
        </button>
      </div>

      {showForm && (
        <section className="panel contract-form">

          <div className="panel-header">
            <div>
              <h3>Create Data Exchange</h3>
              <p>
                Start a governed data exchange between government systems.
              </p>
            </div>
          </div>

          <div className="form-grid">

            <div>
              <label>Source System</label>

              <select
                value={source}
                onChange={(e) => setSource(e.target.value)}
              >
                <option>Employment</option>
                <option>Welfare</option>
                <option>Benefits</option>
              </select>
            </div>

            <div>
              <label>Target System</label>

              <select
                value={target}
                onChange={(e) => setTarget(e.target.value)}
              >
                <option>Welfare</option>
                <option>Benefits</option>
                <option>Employment</option>
              </select>
            </div>

            <div>
              <label>Number of Records</label>

              <input
                type="number"
                min="1"
                value={records}
                onChange={(e) => setRecords(e.target.value)}
              />
            </div>

          </div>

          <div className="form-actions">

            <button
              className="secondary-button"
              onClick={() => setShowForm(false)}
            >
              Cancel
            </button>

            <button
              className="primary-button"
              onClick={createExchange}
            >
              Start Exchange
            </button>

          </div>

        </section>
      )}

      <div className="exchange-summary">

        <div>
          <span>Total Exchanges</span>
          <strong>{exchanges.length}</strong>
        </div>

        <div>
          <span>Successful</span>
          <strong>
            {exchanges.filter((e) => e.status === "Completed").length}
          </strong>
        </div>

        <div>
          <span>Processing</span>
          <strong>
            {exchanges.filter((e) => e.status === "Processing").length}
          </strong>
        </div>

        <div>
          <span>Failed</span>
          <strong>
            {exchanges.filter((e) => e.status === "Failed").length}
          </strong>
        </div>

      </div>

      <section className="panel">

        <div className="panel-header">
          <div>
            <h3>Recent Data Exchanges</h3>
            <p>
              Cross-department data transfer activity.
            </p>
          </div>
        </div>

        <div className="exchange-table">

          <div className="exchange-row exchange-heading">
            <span>Exchange ID</span>
            <span>Data Flow</span>
            <span>Records</span>
            <span>Status</span>
            <span>Time</span>
          </div>

          {exchanges.map((exchange) => (

            <div className="exchange-row" key={exchange.id}>

              <strong>{exchange.id}</strong>

              <span>{exchange.flow}</span>

              <span>{exchange.records}</span>

              <span
                className={
                  exchange.status === "Completed"
                    ? "status success"
                    : exchange.status === "Failed"
                    ? "status failed"
                    : "status processing"
                }
              >
                ● {exchange.status}
              </span>

              <span>{exchange.time}</span>

            </div>

          ))}

        </div>

      </section>

    </div>
  );
}
/* =========================
   AUDIT LOGS
========================= */

function AuditLogs() {
  const [logs] = useState([
    {
      id: "LOG-9821",
      event: "Data Exchange",
      user: "admin@govmesh.gov",
      flow: "Employment → Welfare",
      status: "Success",
      time: "Today 10:42 AM",
    },
    {
      id: "LOG-9820",
      event: "Mapping Approved",
      user: "reviewer@govmesh.gov",
      flow: "Employment → Welfare",
      status: "Success",
      time: "Today 10:35 AM",
    },
    {
      id: "LOG-9819",
      event: "Schema Discovered",
      user: "admin@govmesh.gov",
      flow: "Benefits → Welfare",
      status: "Success",
      time: "Today 10:18 AM",
    },
    {
      id: "LOG-9818",
      event: "Data Exchange",
      user: "system@govmesh.gov",
      flow: "Benefits → Welfare",
      status: "Failed",
      time: "Today 09:54 AM",
    },
  ]);

  const exportLogs = () => {
    const headers = [
      "Log ID",
      "Event",
      "User",
      "Data Flow",
      "Status",
      "Time",
    ];

    const rows = logs.map((log) => [
      log.id,
      log.event,
      log.user,
      log.flow,
      log.status,
      log.time,
    ]);

    const csvContent = [
      headers,
      ...rows,
    ]
      .map((row) =>
        row.map((value) => `"${value}"`).join(",")
      )
      .join("\n");

    const blob = new Blob([csvContent], {
      type: "text/csv;charset=utf-8;",
    });

    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.download = "govmesh-audit-logs.csv";
    link.click();

    URL.revokeObjectURL(url);
  };

  return (
    <div className="dashboard">

      <div className="welcome">
        <div>
          <h2>Audit Logs</h2>
          <p>
            Track system activity, data exchanges and administrative actions.
          </p>
        </div>

        <button
          className="primary-button"
          onClick={exportLogs}
        >
          ↓ Export Logs
        </button>
      </div>

      <div className="audit-summary">

        <div>
          <span>Total Events</span>
          <strong>8,421</strong>
        </div>

        <div>
          <span>Successful</span>
          <strong>8,297</strong>
        </div>

        <div>
          <span>Failed</span>
          <strong>124</strong>
        </div>

      </div>

      <section className="panel">

        <div className="panel-header">
          <div>
            <h3>Recent Audit Events</h3>
            <p>
              Immutable record of important GovMesh activities.
            </p>
          </div>
        </div>

        <div className="audit-table">

          <div className="audit-row audit-heading">
            <span>Log ID</span>
            <span>Event</span>
            <span>User</span>
            <span>Data Flow</span>
            <span>Status</span>
            <span>Time</span>
          </div>

          {logs.map((log) => (

            <div className="audit-row" key={log.id}>

              <strong>{log.id}</strong>

              <span>{log.event}</span>

              <span>{log.user}</span>

              <span>{log.flow}</span>

              <span
                className={
                  log.status === "Success"
                    ? "status success"
                    : "status failed"
                }
              >
                ● {log.status}
              </span>

              <span>{log.time}</span>

            </div>

          ))}

        </div>

      </section>

    </div>
  );
}
/* =========================
   COMING SOON
========================= */

function ComingSoon({ page }) {
  return (
    <div className="coming-soon">
      <div className="coming-icon">⚙</div>

      <h2>{page}</h2>

      <p>
        This module is being built as part of the GovMesh platform.
      </p>

      <span>M4 Frontend</span>
    </div>
  );
}

export default App;