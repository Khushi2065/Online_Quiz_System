-- Create the database
drop database quiz_db;
CREATE DATABASE IF NOT EXISTS quiz_db;
USE quiz_db;

-- Create users table (optional, if you want login in future)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50)
);

-- Create quizzes table
CREATE TABLE IF NOT EXISTS quizzes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL
);

-- Create questions table
CREATE TABLE IF NOT EXISTS questions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id INT,
    question_text TEXT,
    option_a VARCHAR(100),
    option_b VARCHAR(100),
    option_c VARCHAR(100),
    option_d VARCHAR(100),
    correct_option CHAR(1),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Create results table (if you want to track score later)
CREATE TABLE IF NOT EXISTS results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    quiz_id INT,
    score INT,
    total INT,
    taken_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- Insert default quiz
INSERT INTO quizzes (title) VALUES ('General Knowledge');

-- Insert sample questions for quiz_id = 1
INSERT INTO questions (
    quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option
) VALUES
(1, 'What is the capital of India?', 'Mumbai', 'Delhi', 'Kolkata', 'Chennai', 'B'),
(1, 'What is 2 + 2?', '3', '4', '5', '6', 'B'),
(1, 'Who is the Father of the Nation (India)?', 'Nehru', 'Bose', 'Gandhi', 'Patel', 'C'),
(1, 'Which planet is known as the Red Planet?', 'Earth', 'Venus', 'Jupiter', 'Mars', 'D'),
(1, 'What is the largest ocean?', 'Atlantic', 'Indian', 'Arctic', 'Pacific', 'D'),
(1, 'Which planet is known as the Red Planet?', 'Earth', 'Mars', 'Jupiter', 'Saturn', 'B'),
(1, 'Who wrote the national anthem of India?', 'Rabindranath Tagore', 'Bankim Chandra Chatterjee', 'Sarojini Naidu', 'Mahatma Gandhi', 'A'),
(1, 'Which gas do plants absorb from the atmosphere?', 'Oxygen', 'Hydrogen', 'Nitrogen', 'Carbon Dioxide', 'D'),
(1, 'How many continents are there in the world?', '5', '6', '7', '8', 'C'),
(1, 'What is the currency of Japan?', 'Yuan', 'Yen', 'Won', 'Ringgit', 'B'),
(1, 'Who invented the light bulb?', 'Nikola Tesla', 'Albert Einstein', 'Thomas Edison', 'Isaac Newton', 'C'),
(1, 'Which is the largest ocean on Earth?', 'Atlantic Ocean', 'Indian Ocean', 'Arctic Ocean', 'Pacific Ocean', 'D'),
(1, 'What is the hardest natural substance?', 'Gold', 'Iron', 'Diamond', 'Graphite', 'C'),
(1, 'Which is the longest river in the world?', 'Amazon', 'Ganga', 'Nile', 'Yangtze', 'C'),
(1, 'Which day is celebrated as World Environment Day?', '5 June', '22 April', '8 March', '1 December', 'A');

-- (Optional) Add test user for login
INSERT INTO users (username, password) VALUES ('admin', '1234');
