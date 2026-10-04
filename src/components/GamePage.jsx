import React, { useState, useEffect, useRef } from 'react';
import { recordReaction } from '../services/api';

// Generate robust unique session ID
function generateSessionId() {
  if (typeof crypto !== 'undefined' && crypto.randomUUID) {
    return `reaction-${crypto.randomUUID()}`;
  }
  return `reaction-${Date.now()}-${Math.floor(Math.random() * 10000)}`;
}

export default function GamePage({ onGameComplete }) {
  // Game state: 'start' | 'waiting' | 'too_early' | 'target' | 'round_result' | 'final_result'
  const [gameState, setGameState] = useState('start');
  const [currentRound, setCurrentRound] = useState(1);
  const [reactionTimes, setReactionTimes] = useState([]);
  const [latestReactionTime, setLatestReactionTime] = useState(null);
  const [targetPosition, setTargetPosition] = useState({ top: '50%', left: '50%' });

  // Connection mode state: 'DIRECT' (Normal) | 'GA' (Global Accelerator)
  const [connectionMode, setConnectionMode] = useState('DIRECT');

  const gameAreaRef = useRef(null);
  const targetShownTimeRef = useRef(0);
  const timerRef = useRef(null);
  const sessionIdRef = useRef(generateSessionId());

  useEffect(() => {
    return () => {
      if (timerRef.current) clearTimeout(timerRef.current);
    };
  }, []);

  const startRound = () => {
    setGameState('waiting');
    const delay = 1000 + Math.random() * 2000;

    if (timerRef.current) clearTimeout(timerRef.current);

    timerRef.current = setTimeout(() => {
      if (gameAreaRef.current) {
        const rect = gameAreaRef.current.getBoundingClientRect();
        const targetSize = 72;
        const maxLeft = Math.max(10, rect.width - targetSize - 20);
        const maxTop = Math.max(10, rect.height - targetSize - 20);

        const randomLeft = Math.floor(10 + Math.random() * maxLeft);
        const randomTop = Math.floor(10 + Math.random() * maxTop);

        setTargetPosition({ top: `${randomTop}px`, left: `${randomLeft}px` });
      }

      targetShownTimeRef.current = performance.now();
      setGameState('target');
    }, delay);
  };

  const handleAreaClick = () => {
    if (gameState === 'waiting') {
      if (timerRef.current) clearTimeout(timerRef.current);
      setGameState('too_early');
    }
  };

  const handleTargetClick = (e) => {
    e.stopPropagation();
    if (gameState !== 'target') return;

    // 1. Calculate reaction time in browser locally
    const clickedAt = performance.now();
    const elapsed = Math.round(clickedAt - targetShownTimeRef.current);

    // 2. Show result immediately to user (zero network delay)
    setLatestReactionTime(elapsed);
    const updatedTimes = [...reactionTimes, elapsed];
    setReactionTimes(updatedTimes);
    setGameState('round_result');

    // 3. Send payload asynchronously to backend with chosen connectionType
    recordReaction({
      sessionId: sessionIdRef.current,
      roundNumber: currentRound,
      reactionTime: elapsed,
      targetShownAt: new Date(Date.now() - elapsed).toISOString(),
      clickedAt: new Date().toISOString(),
      connectionType: connectionMode,
    });
  };

  const handleContinue = () => {
    if (currentRound < 5) {
      setCurrentRound((prev) => prev + 1);
      startRound();
    } else {
      setGameState('final_result');
      if (onGameComplete) {
        onGameComplete(reactionTimes);
      }
    }
  };

  const handlePlayAgain = () => {
    setCurrentRound(1);
    setReactionTimes([]);
    setLatestReactionTime(null);
    sessionIdRef.current = generateSessionId();
    setGameState('start');
  };

  const averageTime = reactionTimes.length > 0
    ? Math.round(reactionTimes.reduce((a, b) => a + b, 0) / reactionTimes.length)
    : 0;
  const fastestTime = reactionTimes.length > 0 ? Math.min(...reactionTimes) : 0;
  const slowestTime = reactionTimes.length > 0 ? Math.max(...reactionTimes) : 0;

  return (
    <div className="game-container" style={{ textAlign: 'center', maxWidth: '640px', margin: '0 auto' }}>
      <div style={{ marginBottom: '20px' }}>
        <h1 className="title-large" style={{ letterSpacing: '-0.03em' }}>REACTION</h1>
        <p className="subtitle-text">Test your reflexes.</p>
      </div>

      {/* Connection Mode Switch */}
      <div style={{ marginBottom: '24px', display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '8px' }}>
        <div style={{ fontSize: '13px', fontWeight: '500', color: 'var(--text-secondary)', letterSpacing: '0.02em' }}>
          Connection
        </div>
        <div
          style={{
            display: 'inline-flex',
            background: 'var(--bg-secondary)',
            padding: '4px',
            borderRadius: 'var(--radius-pill)',
            border: '1px solid var(--border-subtle)',
          }}
        >
          <button
            onClick={() => setConnectionMode('DIRECT')}
            style={{
              padding: '6px 18px',
              fontSize: '13px',
              fontWeight: connectionMode === 'DIRECT' ? '600' : '400',
              borderRadius: 'var(--radius-pill)',
              border: 'none',
              background: connectionMode === 'DIRECT' ? 'var(--bg-primary)' : 'transparent',
              color: connectionMode === 'DIRECT' ? 'var(--text-primary)' : 'var(--text-secondary)',
              boxShadow: connectionMode === 'DIRECT' ? '0 1px 3px rgba(0,0,0,0.08)' : 'none',
              cursor: 'pointer',
              transition: 'all 0.15s ease',
            }}
          >
            Normal
          </button>
          <button
            onClick={() => setConnectionMode('GA')}
            style={{
              padding: '6px 18px',
              fontSize: '13px',
              fontWeight: connectionMode === 'GA' ? '600' : '400',
              borderRadius: 'var(--radius-pill)',
              border: 'none',
              background: connectionMode === 'GA' ? 'var(--bg-primary)' : 'transparent',
              color: connectionMode === 'GA' ? 'var(--accent-color)' : 'var(--text-secondary)',
              boxShadow: connectionMode === 'GA' ? '0 1px 3px rgba(0,0,0,0.08)' : 'none',
              cursor: 'pointer',
              transition: 'all 0.15s ease',
            }}
          >
            Global Accelerator
          </button>
        </div>
      </div>

      <div
        ref={gameAreaRef}
        onClick={handleAreaClick}
        className="apple-card"
        style={{
          position: 'relative',
          minHeight: '360px',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          userSelect: 'none',
          overflow: 'hidden',
          background: gameState === 'waiting' ? '#fafafa' : 'var(--bg-secondary)',
          cursor: gameState === 'waiting' ? 'pointer' : 'default',
        }}
      >
        {gameState === 'start' && (
          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '16px' }}>
            <div
              style={{
                width: '64px',
                height: '64px',
                borderRadius: '50%',
                border: '2px solid var(--border-strong)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                background: 'var(--bg-primary)',
              }}
            >
              <div style={{ width: '24px', height: '24px', borderRadius: '50%', background: 'var(--accent-color)' }} />
            </div>
            <p style={{ color: 'var(--text-secondary)', fontSize: '15px' }}>
              Click the target as soon as it appears.
            </p>
            <button className="btn-primary" onClick={startRound} style={{ marginTop: '8px', width: '140px' }}>
              Start
            </button>
          </div>
        )}

        {gameState === 'waiting' && (
          <div style={{ textAlign: 'center' }}>
            <h2 className="title-medium" style={{ color: 'var(--text-secondary)' }}>Wait...</h2>
            <p style={{ color: 'var(--text-tertiary)', fontSize: '14px', marginTop: '4px' }}>
              Don't click yet.
            </p>
          </div>
        )}

        {gameState === 'too_early' && (
          <div style={{ textAlign: 'center' }}>
            <h2 className="title-medium" style={{ color: 'var(--danger-color)' }}>Too early.</h2>
            <p style={{ color: 'var(--text-secondary)', fontSize: '14px', marginTop: '6px', marginBottom: '20px' }}>
              You clicked before the target appeared.
            </p>
            <button className="btn-secondary" onClick={startRound}>
              Try Round {currentRound} Again
            </button>
          </div>
        )}

        {gameState === 'target' && (
          <div
            className="animate-target-appear"
            onClick={handleTargetClick}
            style={{
              position: 'absolute',
              top: targetPosition.top,
              left: targetPosition.left,
              width: '72px',
              height: '72px',
              borderRadius: '50%',
              border: '3px solid var(--accent-color)',
              background: 'rgba(0, 113, 227, 0.08)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              cursor: 'pointer',
              boxShadow: '0 4px 20px rgba(0, 113, 227, 0.25)',
            }}
          >
            <div
              style={{
                width: '42px',
                height: '42px',
                borderRadius: '50%',
                border: '2px solid var(--accent-color)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              <div style={{ width: '16px', height: '16px', borderRadius: '50%', background: 'var(--accent-color)' }} />
            </div>
          </div>
        )}

        {gameState === 'round_result' && (
          <div style={{ textAlign: 'center' }}>
            <div style={{ fontSize: '56px', fontWeight: '700', letterSpacing: '-0.04em', color: 'var(--text-primary)' }}>
              {latestReactionTime} <span style={{ fontSize: '24px', fontWeight: '500', color: 'var(--text-secondary)' }}>ms</span>
            </div>
            <p style={{ color: 'var(--text-secondary)', fontSize: '15px', marginTop: '-4px' }}>
              Your reaction time
            </p>
            <div
              style={{
                display: 'inline-block',
                margin: '16px 0 24px',
                padding: '4px 14px',
                background: 'var(--bg-primary)',
                borderRadius: 'var(--radius-pill)',
                fontSize: '13px',
                fontWeight: '600',
                color: 'var(--text-secondary)',
                letterSpacing: '0.05em',
              }}
            >
              ROUND {currentRound} / 5
            </div>
            <div>
              <button className="btn-primary" onClick={handleContinue} style={{ width: '150px' }}>
                Continue
              </button>
            </div>
          </div>
        )}

        {gameState === 'final_result' && (
          <div style={{ width: '100%', maxWidth: '420px', padding: '10px 0' }}>
            <h2 className="title-medium" style={{ color: 'var(--text-secondary)', fontSize: '18px' }}>Great job.</h2>
            <div style={{ fontSize: '52px', fontWeight: '700', letterSpacing: '-0.04em', margin: '4px 0 -4px' }}>
              {averageTime} <span style={{ fontSize: '22px', fontWeight: '500', color: 'var(--text-secondary)' }}>ms</span>
            </div>
            <p style={{ color: 'var(--text-secondary)', fontSize: '14px', marginBottom: '24px' }}>
              Average reaction time ({connectionMode === 'GA' ? 'Global Accelerator' : 'Normal'})
            </p>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px', marginBottom: '24px' }}>
              <div style={{ background: 'var(--bg-primary)', padding: '12px', borderRadius: 'var(--radius-md)' }}>
                <div style={{ fontSize: '12px', color: 'var(--text-secondary)', fontWeight: '500' }}>Fastest</div>
                <div style={{ fontSize: '20px', fontWeight: '600', marginTop: '2px' }}>{fastestTime} ms</div>
              </div>
              <div style={{ background: 'var(--bg-primary)', padding: '12px', borderRadius: 'var(--radius-md)' }}>
                <div style={{ fontSize: '12px', color: 'var(--text-secondary)', fontWeight: '500' }}>Slowest</div>
                <div style={{ fontSize: '20px', fontWeight: '600', marginTop: '2px' }}>{slowestTime} ms</div>
              </div>
            </div>

            <div style={{ borderTop: '1px solid var(--border-subtle)', paddingTop: '16px', marginBottom: '24px' }}>
              {reactionTimes.map((time, idx) => (
                <div
                  key={idx}
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    padding: '6px 4px',
                    fontSize: '14px',
                    color: 'var(--text-secondary)',
                    borderBottom: idx < reactionTimes.length - 1 ? '1px solid rgba(0,0,0,0.04)' : 'none',
                  }}
                >
                  <span>Round {idx + 1}</span>
                  <span style={{ fontWeight: '600', color: 'var(--text-primary)' }}>{time} ms</span>
                </div>
              ))}
            </div>

            <button className="btn-primary" onClick={handlePlayAgain} style={{ width: '160px' }}>
              Play Again
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
