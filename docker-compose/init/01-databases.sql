CREATE DATABASE IF NOT EXISTS robodance_auth;
CREATE DATABASE IF NOT EXISTS robodance_workouts;
CREATE DATABASE IF NOT EXISTS robodance_progress;

CREATE USER IF NOT EXISTS 'robodance'@'%' IDENTIFIED BY 'robodance';

GRANT ALL PRIVILEGES ON robodance_auth.* TO 'robodance'@'%';
GRANT ALL PRIVILEGES ON robodance_workouts.* TO 'robodance'@'%';
GRANT ALL PRIVILEGES ON robodance_progress.* TO 'robodance'@'%';

FLUSH PRIVILEGES;
