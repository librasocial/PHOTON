import React, { useEffect } from "react";
import "../css/Dashboard.css";
import "../css/Vitals.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import "../../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../../node_modules/bootstrap/dist/js/bootstrap.bundle";
import LiveService from "./LabModule/Services/LiveService";

export default function HomePage(props) {
  useEffect(() => {
    document.title = "EMR Home Screen";
  }, []);

  const date = new Date();
  let hours = date.getHours();
  const timeOfDay = `Good ${
    (hours < 12 && "Morning") || (hours < 18 && "Afternoon") || "Evening"
  }`;

  return (
    <React.Fragment>
      <div className="div service-tab-div">
        <div style={{ minHeight: "82vh" }}>
          <LiveService />
        </div>
      </div>
    </React.Fragment>
  );
}
