import React, { useState } from 'react';
import Navbar from './components/Navbar';
import GamePage from './components/GamePage';
import StatsPage from './components/StatsPage';

export default function App() {
  const [activeTab, setActiveTab] = useState('game'); // Default page is GAME
  const [userGameData, setUserGameData] = useState([]);

  const handleGameComplete = (times) => {
    setUserGameData(times);
  };

  return (
    <div className="app-container">
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />
      
      <main className="main-content">
        {activeTab === 'game' ? (
          <GamePage onGameComplete={handleGameComplete} />
        ) : (
          <StatsPage userGameData={userGameData} />
        )}
      </main>
    </div>
  );
}
