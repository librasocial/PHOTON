import React, { useState, useEffect, useRef } from "react";
import { Col, Row, Form, Button, Breadcrumb, Card } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import SubCenterModal from "./SubCenterModal";
import { loadSubCenters } from "../../../../redux/actions";
import {
  loadSubCenterDetails,
  loadUpdateSubCenter,
} from "../../../../redux/phcAction";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import PageLoader from "../../../PageLoader";
import DisplayDataRow from "../../DisplayDataRow/DisplayDataRow";
import { useHistory } from "react-router-dom";

export default function AddSubCenter(props) {
  const removevitalstorege = () => {
    window.history.back();
  };

  let dispatch = useDispatch();
  let phcuuid = sessionStorage.getItem("uuidofphc");

  const { subCentersList } = useSelector((state) => state.data);

  const [addSubCenter, setAddSubCenter] = useState(false);
  const [subCenterShow, setSubCenterShow] = useState(false);
  const handleSubCenter = (e) => {
    setAddSubCenter(true);
  };

  const handleSubCenterClose = () => {
    setSubCenterShow(false);
  };

  const removeSubCenter = (e) => {
    setAddSubCenter(false);
  };

  const [idForUpdate, setIdForUpdate] = useState(false);
  const createSubCenter = () => setSubCenterShow(true);
  const updateSubCenter = (subUuid) => {
    setSubCenterShow(true);
    setIdForUpdate(subUuid);
    dispatch(loadSubCenterDetails(subUuid));
  };

  useEffect(() => {
    document.title = "EMR Super Admin Manage Sub-Center";
    dispatch(loadSubCenters(phcuuid));
  }, []);

  const [displayData, setDisplayData] = useState([]);
  useEffect(() => {
    if (subCentersList) {
      let dataArray = [];
      subCentersList.forEach((element) => {
        if (element?.target?.properties?.status == "ACTIVE") {
          dataArray.push({
            name: element?.target?.properties?.name,
            photo: element?.target?.properties?.photo,
            code: element?.target?.properties?.code,
            contact: element?.target?.properties?.contact,
            status: true,
            uuid: element?.target?.properties?.uuid,
          });
        } else if (element?.target?.properties?.status == "INACTIVE") {
          dataArray.push({
            name: element?.target?.properties?.name,
            photo: element?.target?.properties?.photo,
            code: element?.target?.properties?.code,
            contact: element?.target?.properties?.contact,
            status: false,
            uuid: element?.target?.properties?.uuid,
          });
        } else {
          dataArray.push({
            name: element?.target?.properties?.name,
            photo: element?.target?.properties?.photo,
            code: element?.target?.properties?.code,
            contact: element?.target?.properties?.contact,
            status: false,
            uuid: element?.target?.properties?.uuid,
          });
        }
        setDisplayData(dataArray);
      });
    }
  }, [subCentersList]);

  const [pageLoader, setPageLoader] = useState(false);
  let villageList = [];
  const setSubCenterEnable = (e, i, uuid) => {
    const list = [...displayData];

    let status;
    if (displayData[i].status == true) {
      status = "INACTIVE";
    } else if (displayData[i].status == false) {
      status = "ACTIVE";
    }

    list[i].status = !displayData[i].status;
    setDisplayData(list);
    setPageLoader(true);

    var updateData = {
      type: "SubCenter",
      properties: {
        status: status,
      },
    };
    var requestOptions = {
      headers: serviceHeaders.myHeaders1,
      method: "PATCH",
      mode: "cors",
      body: JSON.stringify(updateData),
    };
    dispatch(
      loadUpdateSubCenter(uuid, requestOptions, villageList, setPageLoader)
    );
  };

  let history = useHistory();
  const removeCurrentPage = () => {
    history.go(-2);
  };

  return (
    <React.Fragment>
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <SubCenterModal
        subCenterShow={subCenterShow}
        setSubCenterShow={setSubCenterShow}
        handleSubCenterClose={setSubCenterShow}
        idForUpdate={idForUpdate}
        setIdForUpdate={setIdForUpdate}
      />
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
                  Add & Manage Sub-Centres/HWC
                </Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="form-col">
              <Form className="super-admin-form">
                <div className="assign-complaint">
                  <h3 className="super-phc">Add & Manage Sub-Centres/HWC</h3>
                  <Row className="search-add-staff">
                    <Col lg={6} md={6}>
                      <div className="form-group sta-search">
                        <input
                          type="text"
                          className="form-control staff-search"
                          placeholder="Search Sub-Centres/HWC Name"
                        />
                        <span className="fa fa-search form-control-pro-feedback d-flex"></span>
                      </div>
                    </Col>
                    <Col lg={6} md={6} className="create-phc">
                      <div className="save-btn-section">
                        <SaveButton
                          butttonClick={handleSubCenter}
                          class_name="regBtnN"
                          button_name="Add New Sub-Center/HWC"
                        />
                      </div>
                    </Col>
                  </Row>
                </div>
                {!displayData && !addSubCenter ? (
                  <div align="center">
                    <img
                      src="../img/super/add-hospital.png"
                      className="staff-image"
                    />
                  </div>
                ) : (
                  <DisplayDataRow
                    displayData={displayData}
                    page="Sub-Center"
                    setStatusEnable={setSubCenterEnable}
                    openModale={createSubCenter}
                    addNewRow={addSubCenter}
                    removeRow={removeSubCenter}
                    updateData={updateSubCenter}
                  />
                )}
              </Form>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
