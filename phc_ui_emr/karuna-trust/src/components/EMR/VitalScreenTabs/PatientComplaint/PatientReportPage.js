import React from "react";
import { Form } from "react-bootstrap";

export default function PatientReportPage(props) {
  return (
    <React.Fragment>
      <div className="form-col">
        <Form className="patient-history-form med-history">
          <iframe
            title="Patient report fo Medical"
            width="100%"
            height="363px"
            frameborder="0"
            allowFullScreen="true"
            // src="https://app.powerbi.com/reportEmbed?reportId=1f741dfd-2b20-46c5-8526-f6c56a53f95e&autoAuth=true&ctid=1942072d-23c5-40e7-a005-51fa4fa08042"
          ></iframe>
        </Form>
      </div>
    </React.Fragment>
  );
}
