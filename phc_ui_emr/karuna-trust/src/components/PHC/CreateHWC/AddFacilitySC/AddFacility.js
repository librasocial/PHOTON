import React, { useState, useEffect } from "react";
import { Col, Row, Form, Card, Breadcrumb } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import { loadSubCenters } from "../../../../redux/actions";
import { loadSubCenterDetails } from "../../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import DisplayDataRow from "../../DisplayDataRow/DisplayDataRow";

export default function AddFacility(props) {
  let history = useHistory();
  let dispatch = useDispatch();
  let phcuuid = sessionStorage.getItem("uuidofphc");
  const { subCentersList } = useSelector((state) => state.data);

  const [displayData, setDisplayData] = useState([]);
  useEffect(
    (subUuid) => {
      dispatch(loadSubCenterDetails(subUuid));
      if (subCentersList) {
        let dataArray = [];
        subCentersList.forEach((element) => {
          dataArray.push({
            name: element?.target?.properties?.name,
            photo: element?.target?.properties?.photo,
            code: element?.target?.properties?.code,
            contact: element?.target?.properties?.contact,
            uuid: element?.target?.properties?.uuid,
          });
          setDisplayData(dataArray);
        });
      }
    },
    [subCentersList]
  );

  useEffect(() => {
    dispatch(loadSubCenters(phcuuid));
    document.title = "EMR Super Admin SC/HWC to Create Facility Masters";
  }, [phcuuid]);

  const removevitalstorege = () => {
    window.history.back();
  };

  const [isActive, setIsActive] = useState(false);
  function assignProResponse() {
    setIsActive(true);
  }

  const createsubCenterFac = (e) => {
    dispatch(loadSubCenterDetails(e));
    history.push("/CreateSubFacility");
  };

  const removeCurrentPage = () => {
    history.go(-2);
  };

  return (
    <React.Fragment>
      <div className="div vital-div">
        <div className="regHeader">
          <h1 className="Encounter-Header">PHC Configuration</h1>
          <hr style={{ margin: "0px" }} />
        </div>
        <div className="config-div">
          <div className="super-tab">
            <div className="super-breadcrumb">
              <Breadcrumb>
                <Breadcrumb.Item
                  className="pur-order-breadcrumb"
                  onClick={removeCurrentPage}
                >
                  Dashboard
                </Breadcrumb.Item>
                <Breadcrumb.Item
                  className="pur-order-breadcrumb"
                  onClick={removevitalstorege}
                >
                  PHC Configuration
                </Breadcrumb.Item>
                <Breadcrumb.Item active className="phc-breadcrumb">
                  Select SC/HWCS to Create Facility Masters
                </Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="form-col">
              <Form className="super-admin-form">
                <div className="assign-complaint">
                  <h3 className="super-config-details">
                    Select SC/HWCS to Create Facility Masters
                  </h3>
                </div>
                <Row className="assign-subcenter-details">
                  <DisplayDataRow
                    displayData={displayData}
                    page="SC-Assign-Process"
                    createsubCenterFac={createsubCenterFac}
                  />
                </Row>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
