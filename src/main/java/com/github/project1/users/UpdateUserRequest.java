package com.github.project1.users;

public class UpdateUserRequest {

        private String userId;
        private String givenName;
        private String surname;
        private String username;
        private String email;
        private String password;

        


    public UpdateUserRequest(String userId, String givenName, String surname, String username, String email,
                String password) {
            this.userId = userId;
            this.givenName = givenName;
            this.surname = surname;
            this.username = username;
            this.email = email;
            this.password = password;
        }

    public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGivenName() {
            return givenName;
        }

       public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    public User extractEntity() {
        
        User extractedEntity = new User();
        extractedEntity.setUsername(this.username);
        extractedEntity.setPassword(this.password);
        extractedEntity.setEmail(this.email);
        extractedEntity.setGivenName(this.givenName);
        extractedEntity.setSurname(this.surname);
        return extractedEntity;
    }

    @Override
    public String toString() {
        return "UpdateUserRequest [email=" + email + ", givenName=" + givenName + ", password=" + password
                + ", surname=" + surname + ", userId=" + userId + ", username=" + username + "]";
    }
    
    

}
