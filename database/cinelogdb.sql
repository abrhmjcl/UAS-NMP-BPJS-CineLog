CREATE DATABASE IF NOT EXISTS `cinelogdb`;
USE `cinelogdb`;

DROP TABLE IF EXISTS `movies`;
CREATE TABLE `movies` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(150) NOT NULL,
  `genre` VARCHAR(50) NOT NULL,
  `platform` VARCHAR(50) NOT NULL,
  `age_rating` VARCHAR(10) NOT NULL DEFAULT 'SU',
  `release_year` INT(4) NOT NULL DEFAULT 2024,
  `rating` DECIMAL(3,1) NOT NULL DEFAULT 0.0,
  `url` TEXT NOT NULL,
  `trailer_url` TEXT NOT NULL,
  `review` TEXT NOT NULL,
  `is_available` TINYINT(1) NOT NULL DEFAULT 1,
  `has_spoilers` TINYINT(1) NOT NULL DEFAULT 0,
  `is_favorite` TINYINT(1) NOT NULL DEFAULT 0,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `movies` (`id`, `title`, `genre`, `platform`, `age_rating`, `release_year`, `rating`, `url`, `trailer_url`, `review`, `is_available`, `has_spoilers`, `is_favorite`) VALUES
(1, 'Interstellar', 'Sci-Fi', 'Cinema XXI', '13+', 2014, 8.7, 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg', 'https://www.youtube.com/watch?v=zSWdZVtXT7E', 'Absolute Cinema Christoper Nolan', 1, 0, 1),
(2, 'Oppenheimer', 'Drama', 'Cinema XXI', '17+', 2023, 8.9, 'https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg', 'https://www.youtube.com/watch?v=uYPbbksJxIg', 'Awal mula bom atom', 1, 1, 1),
(3, 'Dune: Part Two', 'Sci-Fi', 'Cinema XXI', '13+', 2024, 8.6, 'https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg', 'https://www.youtube.com/watch?v=Way9Dexny3w', 'Sekuel terbaru dari nue part 1', 0, 0, 0),
(4, 'Spirited Away', 'Anime', 'Netflix', 'SU', 2001, 8.6, 'https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg', 'https://www.youtube.com/watch?v=ByXuk9QqQkk', 'Animasi terbaru dari Studio Gibli', 1, 0, 1);