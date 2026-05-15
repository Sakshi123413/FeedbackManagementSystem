// Test BCrypt hash validity
const bcrypt = require('bcrypt');

const storedHash = '$2a$10$EqKcp1WFKVQISheBxmXNOe9r6YkiVQupMBnMRPx0n7c5n2nFzSuKu';
const testPassword = 'password123';

console.log('Testing BCrypt password matching...');
console.log('Stored hash:', storedHash);
console.log('Test password:', testPassword);

bcrypt.compare(testPassword, storedHash, (err, result) => {
    if (err) {
        console.error('Error:', err);
    } else {
        console.log('Password matches:', result);
    }
});
