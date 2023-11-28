import React, { useState, useEffect } from "react";
import { Col, Row, Modal, Card } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { loadAllStaffData, loadCreateOwner } from "../../../../redux/phcAction";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import DisplayDataRow from "../../DisplayDataRow/DisplayDataRow";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";

export default function AssignModal(props) {
  let dispatch = useDispatch();
  const { staffData } = useSelector((state) => state.phcData);
  let phcuuid = sessionStorage.getItem("uuidofphc");

  useEffect(() => {
    dispatch(loadAllStaffData(phcuuid));
  }, [phcuuid]);

  const [isActive, setIsActive] = useState(false);
  function assignProResponse() {
    setIsActive(true);
  }
  const [displayData, setDisplayData] = useState([]);

  useEffect(() => {
    if (staffData) {
      let dataArray = [];
      staffData.forEach((element) => {
        if (element?.targetNode?.properties?.status == "ACTIVE") {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: true,
            uuid: element?.targetNode?.properties?.uuid,
          });
        } else if (element?.targetNode?.properties?.status == "INACTIVE") {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: false,
            uuid: element?.targetNode?.properties?.uuid,
          });
        } else {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: false,
            uuid: element?.targetNode?.properties?.uuid,
          });
        }
        setDisplayData(dataArray);
      });
    }
  }, [staffData]);

  const [radioState, setRadioState] = useState("");
  const [assignedStaff, setAssignedStaff] = useState("");
  const updateRadioGroup = (i, items) => {
    setRadioState(i);
    setAssignedStaff(items);
  };

  const assignOwner = () => {
    var postOwner = {
      relationship: {
        type: "OWNEROF",
      },
      source: {
        type: props.assignRolesValue.name,
        properties: {
          uuid: props.assignRolesValue.uuid,
        },
      },
      target: {
        type: "Process",
        properties: {
          uuid: assignedStaff.uuid,
        },
      },
    };

    var responseRequest = {
      headers: serviceHeaders.myHeaders1,
      method: "POST",
      mode: "cors",
      body: JSON.stringify(postOwner),
    };
    dispatch(loadCreateOwner(responseRequest, props.facilityWindowClose));
  };

  return (
    <React.Fragment>
      <Modal
        show={props.assignFacilityWindow}
        onHide={props.facilityWindowClose}
        className="assign-process-div"
      >
        <div className="assign-header-div">
          <h5 className="assign-process-header">
            Select from the List of Staff to Make them Process Owner{" "}
            <i
              className="fa fa-close close-btn-style"
              onClick={props.facilityWindowClose}
            ></i>
          </h5>
        </div>
        <Row className="assign-staff-details">
          {!displayData && !addStaff ? (
            <div align="center">
              <img src="../img/super/user.png" className="staff-image" />
            </div>
          ) : (
            <DisplayDataRow
              displayData={displayData}
              page="Assign-Process"
              updateRadioGroup={updateRadioGroup}
              radioState={radioState}
            />
          )}
        </Row>
        <div className="assign-pro-btn">
          <div className="save-btn-section">
            <SaveButton
              butttonClick={props.facilityWindowClose}
              class_name="regBtnPC"
              button_name="Cancel"
            />
          </div>
          <div className="save-btn-section">
            <SaveButton
              butttonClick={assignOwner}
              class_name="regBtnN"
              button_name="Save"
            />
          </div>
        </div>
      </Modal>
    </React.Fragment>
  );
}
