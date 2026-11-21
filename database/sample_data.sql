USE hospital_management;
-- Demo seed data. Use only in a development/demo database.
INSERT INTO departments (department_name, description, department_contact) VALUES
('Cardiology','Heart and cardiovascular care','Extension 101'),
('General Medicine','General medical care','Extension 102'),
('Orthopedics','Bone and joint care','Extension 103');
INSERT INTO doctors (doctor_name, specialization, department_id, phone_number, email, availability_status) VALUES
('Dr. Ananya Rao','Cardiology',1,'9000000001','ananya.rao@example.test','Available'),
('Dr. Vivek Kumar','General Medicine',2,'9000000002','vivek.kumar@example.test','Available');
INSERT INTO rooms (room_number, room_type, total_beds, available_beds, room_status) VALUES
('101','General Ward',6,6,'AVAILABLE'),('201','Private Room',1,1,'AVAILABLE'),('ICU-1','ICU',2,2,'AVAILABLE');
