-- SQL schema example for Ranked system (MySQL)
    CREATE TABLE IF NOT EXISTS ranked_players (
      uuid VARCHAR(36) PRIMARY KEY,
      username VARCHAR(32),
      elo INT DEFAULT 1000,
      total_wins INT DEFAULT 0,
      total_losses INT DEFAULT 0,
      last_online DATETIME
    );
    CREATE TABLE IF NOT EXISTS ranked_matches (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      player1_uuid VARCHAR(36),
      player2_uuid VARCHAR(36),
      winner_uuid VARCHAR(36),
      tier VARCHAR(64),
      elo_change INT,
      played_at DATETIME DEFAULT CURRENT_TIMESTAMP
    );
    CREATE TABLE IF NOT EXISTS ranked_seasons (
      id VARCHAR(64) PRIMARY KEY,
      displayName VARCHAR(128),
      startDate DATE,
      endDate DATE,
      active BOOLEAN
    );