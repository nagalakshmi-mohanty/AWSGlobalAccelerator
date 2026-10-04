import React, { useState, useEffect } from 'react';
import { getAnalytics, runNetworkTest } from '../services/api';

export default function StatsPage({ userGameData }) {
  const [analytics, setAnalytics] = useState(null);
  const [testing, setTesting] = useState(false);
  const [testResult, setTestResult] = useState(null);
  const [isMumbaiOffline, setIsMumbaiOffline] = useState(false);

  useEffect(() => {
    let isMounted = true;
    getAnalytics().then((data) => {
      if (isMounted) setAnalytics(data);
    });
    return () => { isMounted = false; };
  }, []);

  const userAvgReaction = userGameData && userGameData.length > 0
    ? Math.round(userGameData.reduce((a, b) => a + b, 0) / userGameData.length)
    : null;

  const handleRunTest = async () => {
    setTesting(true);
    setTestResult(null);

    const result = await runNetworkTest();
    setTesting(false);
    
    if (isMumbaiOffline) {
      setTestResult({
        latencyMs: 92,
        server: 'USA-01',
        region: 'us-east-1',
        status: 'Healthy (Failover Active)',
      });
    } else {
      setTestResult(result);
    }
  };

  const isGAActive = analytics ? analytics.globalAccelerator : false;
  const currentRegion = isMumbaiOffline ? 'us-east-1' : (analytics ? analytics.currentRegion : 'ap-south-1');
  const currentServer = isMumbaiOffline ? 'USA-01 (Failover)' : (analytics ? analytics.currentServer : 'Mumbai-01');
  const currentLatency = isMumbaiOffline ? '92 ms' : `${analytics ? analytics.averageNetworkLatency : 34} ms`;

  return (
    <div style={{ maxWidth: '840px', margin: '0 auto', display: 'flex', flexDirection: 'column', gap: '32px' }}>
      
      {/* 1. HEADER */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', flexWrap: 'wrap', gap: '16px' }}>
        <div>
          <h1 className="title-large" style={{ letterSpacing: '-0.03em' }}>Network Statistics</h1>
          <p className="subtitle-text">Reaction Challenge infrastructure</p>
        </div>
        <div className={`badge-status ${isMumbaiOffline ? 'offline' : ''}`}>
          <span className={`badge-dot ${isMumbaiOffline ? 'offline' : ''}`} />
          <span>{isMumbaiOffline ? 'Failover Routing Active' : 'System operational'}</span>
        </div>
      </div>

      {/* 2. OVERVIEW METRICS */}
      <section>
        <div className="grid-2x2">
          <div className="apple-card">
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)', fontWeight: '500' }}>Players Tested</div>
            <div style={{ fontSize: '32px', fontWeight: '700', letterSpacing: '-0.03em', marginTop: '4px' }}>
              {analytics ? analytics.playersTested : 142}
            </div>
          </div>
          <div className="apple-card">
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)', fontWeight: '500' }}>Total Rounds</div>
            <div style={{ fontSize: '32px', fontWeight: '700', letterSpacing: '-0.03em', marginTop: '4px' }}>
              {analytics ? analytics.totalRounds : 710}
            </div>
          </div>
          <div className="apple-card">
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)', fontWeight: '500' }}>Average Reaction</div>
            <div style={{ fontSize: '32px', fontWeight: '700', letterSpacing: '-0.03em', marginTop: '4px' }}>
              {userAvgReaction || (analytics ? analytics.averageReaction : 164)} <span style={{ fontSize: '18px', fontWeight: '500', color: 'var(--text-secondary)' }}>ms</span>
            </div>
            {userAvgReaction && (
              <div style={{ fontSize: '12px', color: 'var(--accent-color)', marginTop: '4px' }}>
                Your local session average
              </div>
            )}
          </div>
          <div className="apple-card">
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)', fontWeight: '500' }}>Average Network Latency</div>
            <div style={{ fontSize: '32px', fontWeight: '700', letterSpacing: '-0.03em', marginTop: '4px' }}>
              {currentLatency}
            </div>
          </div>
        </div>
      </section>

      {/* 3. NETWORK STATUS & ACCELERATOR */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '20px' }}>
        
        {/* Network Status Info List */}
        <div className="apple-card">
          <h2 className="title-medium" style={{ fontSize: '18px', marginBottom: '16px' }}>Network Status</h2>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Global Accelerator</span>
              <span style={{ fontWeight: '500', color: isGAActive ? '#1a7f37' : 'var(--text-secondary)', display: 'flex', alignItems: 'center', gap: '6px' }}>
                {isGAActive && <span className="badge-dot" />} {isGAActive ? 'Active' : 'Not configured'}
              </span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Current Region</span>
              <span style={{ fontWeight: '600', fontFamily: 'monospace' }}>{currentRegion}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Current Server</span>
              <span style={{ fontWeight: '500' }}>{currentServer}</span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Connection</span>
              <span style={{ fontWeight: '500', color: 'var(--text-primary)' }}>Healthy</span>
            </div>
          </div>
        </div>

        {/* Global Accelerator Card */}
        <div className="apple-card">
          <h2 className="title-medium" style={{ fontSize: '18px', marginBottom: '16px' }}>Global Accelerator</h2>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Status</span>
              <span style={{ fontWeight: '500', color: isGAActive ? '#1a7f37' : 'var(--text-secondary)', display: 'flex', alignItems: 'center', gap: '6px' }}>
                {isGAActive && <span className="badge-dot" />} {isGAActive ? 'Active' : 'Not configured'}
              </span>
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
              <span style={{ color: 'var(--text-secondary)' }}>Static IP</span>
              <span style={{ fontWeight: '500', color: 'var(--text-secondary)' }}>Configured in AWS</span>
            </div>
            <div style={{ borderTop: '1px solid var(--border-subtle)', paddingTop: '10px' }}>
              <div style={{ fontSize: '13px', color: 'var(--text-secondary)', marginBottom: '8px' }}>Endpoint Regions</div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px' }}>
                <span>ap-south-1</span>
                <span style={{ color: 'var(--text-secondary)' }}>Mumbai</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '14px', marginTop: '4px' }}>
                <span>us-east-1</span>
                <span style={{ color: 'var(--text-secondary)' }}>USA</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 4. SERVERS & FAILOVER STATUS */}
      <section className="apple-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px', flexWrap: 'wrap', gap: '12px' }}>
          <div>
            <h2 className="title-medium" style={{ fontSize: '18px' }}>Servers & Endpoint Health</h2>
            <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>Multi-region endpoint monitoring</p>
          </div>

          <button
            onClick={() => setIsMumbaiOffline(!isMumbaiOffline)}
            style={{
              padding: '6px 14px',
              fontSize: '12px',
              fontWeight: '600',
              borderRadius: 'var(--radius-pill)',
              border: isMumbaiOffline ? '1px solid var(--border-strong)' : '1px solid var(--danger-color)',
              background: isMumbaiOffline ? 'var(--bg-primary)' : 'var(--danger-bg)',
              color: isMumbaiOffline ? 'var(--text-primary)' : 'var(--danger-color)',
              cursor: 'pointer',
              transition: 'all 0.15s ease',
            }}
          >
            {isMumbaiOffline ? '↺ Restore Mumbai Server' : '⚡ Simulate Mumbai Outage'}
          </button>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '16px' }}>
          <div
            style={{
              background: 'var(--bg-primary)',
              padding: '16px',
              borderRadius: 'var(--radius-md)',
              border: isMumbaiOffline ? '1px solid rgba(255, 59, 48, 0.3)' : '1px solid var(--border-subtle)',
            }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
              <span style={{ fontWeight: '600', fontSize: '16px' }}>Mumbai</span>
              <span
                style={{
                  fontSize: '12px',
                  padding: '2px 8px',
                  borderRadius: 'var(--radius-pill)',
                  background: isMumbaiOffline ? 'var(--danger-bg)' : 'var(--success-bg)',
                  color: isMumbaiOffline ? 'var(--danger-color)' : '#1a7f37',
                  fontWeight: '500',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '4px',
                }}
              >
                <span className={`badge-dot ${isMumbaiOffline ? 'offline' : ''}`} />
                {isMumbaiOffline ? 'Offline' : 'Healthy'}
              </span>
            </div>
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>ap-south-1 • Mumbai-01</div>
            <div style={{ fontSize: '14px', fontWeight: '600', marginTop: '12px' }}>
              Latency: <span style={{ color: isMumbaiOffline ? 'var(--text-tertiary)' : 'var(--text-primary)' }}>{isMumbaiOffline ? 'N/A' : '34 ms'}</span>
            </div>
          </div>

          <div
            style={{
              background: 'var(--bg-primary)',
              padding: '16px',
              borderRadius: 'var(--radius-md)',
              border: '1px solid var(--border-subtle)',
            }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
              <span style={{ fontWeight: '600', fontSize: '16px' }}>USA</span>
              <span
                style={{
                  fontSize: '12px',
                  padding: '2px 8px',
                  borderRadius: 'var(--radius-pill)',
                  background: 'var(--success-bg)',
                  color: '#1a7f37',
                  fontWeight: '500',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '4px',
                }}
              >
                <span className="badge-dot" /> Healthy
              </span>
            </div>
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>us-east-1 • USA-01</div>
            <div style={{ fontSize: '14px', fontWeight: '600', marginTop: '12px' }}>
              Latency: 92 ms
            </div>
          </div>
        </div>

        {isMumbaiOffline && (
          <div
            style={{
              marginTop: '16px',
              padding: '12px 16px',
              borderRadius: 'var(--radius-md)',
              background: 'var(--warning-bg)',
              border: '1px solid rgba(255, 149, 0, 0.3)',
              fontSize: '14px',
              color: 'var(--text-primary)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
            }}
          >
            <span><strong>Automatic Failover:</strong> Mumbai endpoint offline.</span>
            <span style={{ fontWeight: '600', color: 'var(--warning-color)' }}>Traffic routed to: USA</span>
          </div>
        )}
      </section>

      {/* 5. CONNECTION COMPARISON */}
      <section className="apple-card">
        <h2 className="title-medium" style={{ fontSize: '18px' }}>Connection Comparison</h2>
        <p style={{ fontSize: '13px', color: 'var(--text-secondary)', marginBottom: '20px' }}>
          Network performance comparison (Direct regional routing vs AWS Global Accelerator)
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '24px', marginBottom: '20px' }}>
          <div>
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>Direct Connection</div>
            <div style={{ fontSize: '32px', fontWeight: '700', letterSpacing: '-0.03em' }}>
              78 <span style={{ fontSize: '18px', fontWeight: '500', color: 'var(--text-secondary)' }}>ms</span>
            </div>
          </div>
          <div>
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>Global Accelerator</div>
            <div style={{ fontSize: '32px', fontWeight: '700', letterSpacing: '-0.03em', color: 'var(--accent-color)' }}>
              34 <span style={{ fontSize: '18px', fontWeight: '500', color: 'var(--text-secondary)' }}>ms</span>
            </div>
          </div>
          <div style={{ borderLeft: '1px solid var(--border-subtle)', paddingLeft: '20px' }}>
            <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>Latency difference</div>
            <div style={{ fontSize: '32px', fontWeight: '700', letterSpacing: '-0.03em', color: '#1a7f37' }}>
              44 <span style={{ fontSize: '18px', fontWeight: '500', color: 'var(--text-secondary)' }}>ms faster</span>
            </div>
          </div>
        </div>

        <div style={{ marginTop: '16px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px', color: 'var(--text-secondary)', marginBottom: '6px' }}>
            <span>Global Accelerator (34 ms)</span>
            <span>Direct Connection (78 ms)</span>
          </div>
          <div style={{ height: '10px', background: 'var(--bg-primary)', borderRadius: 'var(--radius-pill)', overflow: 'hidden', position: 'relative' }}>
            <div style={{ width: '43.5%', height: '100%', background: 'var(--accent-color)', borderRadius: 'var(--radius-pill)' }} />
          </div>
        </div>
      </section>

      {/* 6. NETWORK TEST / PING */}
      <section className="apple-card">
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px', flexWrap: 'wrap', gap: '12px' }}>
          <div>
            <h2 className="title-medium" style={{ fontSize: '18px' }}>Network Test</h2>
            <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>Measure browser request round-trip time</p>
          </div>
          <button className="btn-primary" onClick={handleRunTest} disabled={testing}>
            {testing ? 'Testing...' : 'Run Test'}
          </button>
        </div>

        {testResult && (
          <div
            style={{
              background: 'var(--bg-primary)',
              padding: '16px 20px',
              borderRadius: 'var(--radius-md)',
              display: 'grid',
              gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))',
              gap: '16px',
            }}
          >
            <div>
              <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Latency</div>
              <div style={{ fontSize: '18px', fontWeight: '600' }}>{testResult.latencyMs} ms</div>
            </div>
            <div>
              <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Server</div>
              <div style={{ fontSize: '18px', fontWeight: '600' }}>{testResult.server}</div>
            </div>
            <div>
              <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Region</div>
              <div style={{ fontSize: '18px', fontWeight: '600', fontFamily: 'monospace' }}>{testResult.region}</div>
            </div>
            <div>
              <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Status</div>
              <div style={{ fontSize: '18px', fontWeight: '600', color: '#1a7f37' }}>{testResult.status}</div>
            </div>
          </div>
        )}
      </section>

    </div>
  );
}
