import React from "react";
import { Button } from "react-bootstrap";

function UserManagement() {
  return (
    <div className="lab-service-tab">
      <div>
        <h3 className="dia-heading">User Management</h3>
      </div>
      <div className="lab-service-data-field">
        <div className="service-visible-screen">
          <img src="./img/info-card-usermanagement.svg" alt="empty data" />
          <div className="service-btn">
            <Button variant="outline-secondary" className="add-lab-servive-btn">
              Add User management
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default UserManagement;
