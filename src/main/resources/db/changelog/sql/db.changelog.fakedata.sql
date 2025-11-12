# -- liquibase formatted sql
#
# -- Sample data for teacher (user_id = 1) - exams, questions, answers
#
# -- ================================================
# -- 1️⃣ TẠO 2 BÀI THI
# -- ================================================
# INSERT INTO exams (name, description, teacher_id, created_by)
# VALUES
#     ('Java Basics Test', 'Basic Java knowledge test for students', 1, 1),
#     ('OOP Principles Test', 'Understanding of core OOP concepts in Java', 1, 1);
#
# -- ================================================
# -- 2️⃣ CÂU HỎI CHO "Java Basics Test"
# -- ================================================
# INSERT INTO questions (content, difficulty, explanation, created_by)
# VALUES
#     ('What is the size of an int in Java?', 'EASY', 'int in Java is 4 bytes', 1),
#     ('Which method is the entry point of any Java program?', 'EASY', 'The main() method is the entry point', 1),
#     ('What is the output of: System.out.println(2 + "2" + 2);', 'MEDIUM', 'String concatenation results in "222"', 1),
#     ('Which keyword prevents inheritance?', 'MEDIUM', 'final keyword prevents inheritance', 1),
#     ('What is the parent class of all classes in Java?', 'HARD', 'The Object class is the root of all classes', 1);
#
# -- Đáp án cho từng câu hỏi
# -- Q1
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('2 bytes', FALSE, 1, 1),
#                                                                        ('4 bytes', TRUE, 1, 1),
#                                                                        ('8 bytes', FALSE, 1, 1),
#                                                                        ('Depends on OS', FALSE, 1, 1);
#
# -- Q2
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('start()', FALSE, 2, 1),
#                                                                        ('run()', FALSE, 2, 1),
#                                                                        ('main()', TRUE, 2, 1),
#                                                                        ('execute()', FALSE, 2, 1);
#
# -- Q3
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('6', FALSE, 3, 1),
#                                                                        ('222', TRUE, 3, 1),
#                                                                        ('24', FALSE, 3, 1),
#                                                                        ('Error', FALSE, 3, 1);
#
# -- Q4
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('abstract', FALSE, 4, 1),
#                                                                        ('private', FALSE, 4, 1),
#                                                                        ('final', TRUE, 4, 1),
#                                                                        ('protected', FALSE, 4, 1);
#
# -- Q5
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('Class', FALSE, 5, 1),
#                                                                        ('System', FALSE, 5, 1),
#                                                                        ('Object', TRUE, 5, 1),
#                                                                        ('Root', FALSE, 5, 1);
#
# -- Liên kết exam1 ↔ các câu hỏi
# INSERT INTO question_exam (exam_id, question_id, point, order_column)
# SELECT e.id, q.id, 2, ROW_NUMBER() OVER ()
# FROM exams e JOIN questions q ON e.name = 'Java Basics Test' AND q.id BETWEEN 1 AND 5;
#
#
# -- ================================================
# -- 3️⃣ CÂU HỎI CHO "OOP Principles Test"
# -- ================================================
# INSERT INTO questions (content, difficulty, explanation, created_by)
# VALUES
#     ('Which OOP concept allows multiple methods with the same name?', 'EASY', 'This is called method overloading', 1),
#     ('What is encapsulation?', 'EASY', 'Wrapping data and methods into a single unit', 1),
#     ('Which keyword is used to achieve inheritance in Java?', 'MEDIUM', 'The extends keyword is used', 1),
#     ('Which OOP concept means "one interface, many implementations"?', 'HARD', 'That is polymorphism', 1),
#     ('What is abstraction in Java?', 'HARD', 'Hiding internal details and showing only functionality', 1);
#
# -- Đáp án cho từng câu hỏi
# -- Q6
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('Inheritance', FALSE, 6, 1),
#                                                                        ('Overloading', TRUE, 6, 1),
#                                                                        ('Overriding', FALSE, 6, 1),
#                                                                        ('Abstraction', FALSE, 6, 1);
#
# -- Q7
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('Separating data and methods', FALSE, 7, 1),
#                                                                        ('Wrapping data and methods together', TRUE, 7, 1),
#                                                                        ('Writing many classes', FALSE, 7, 1),
#                                                                        ('None of the above', FALSE, 7, 1);
#
# -- Q8
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('implements', FALSE, 8, 1),
#                                                                        ('extends', TRUE, 8, 1),
#                                                                        ('super', FALSE, 8, 1),
#                                                                        ('inherits', FALSE, 8, 1);
#
# -- Q9
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('Encapsulation', FALSE, 9, 1),
#                                                                        ('Polymorphism', TRUE, 9, 1),
#                                                                        ('Abstraction', FALSE, 9, 1),
#                                                                        ('Inheritance', FALSE, 9, 1);
#
# -- Q10
# INSERT INTO answers (content, is_correct, question_id, created_by) VALUES
#                                                                        ('Hiding implementation details', TRUE, 10, 1),
#                                                                        ('Showing all details to user', FALSE, 10, 1),
#                                                                        ('Using interfaces only', FALSE, 10, 1),
#                                                                        ('Creating subclasses', FALSE, 10, 1);
#
# -- Liên kết exam2 ↔ các câu hỏi
# INSERT INTO question_exam (exam_id, question_id, point, order_column)
# SELECT e.id, q.id, 2, ROW_NUMBER() OVER ()
# FROM exams e JOIN questions q ON e.name = 'OOP Principles Test' AND q.id BETWEEN 6 AND 10;
