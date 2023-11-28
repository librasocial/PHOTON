import React, { useState, useEffect, useRef } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  Breadcrumb,
  Card,
  Pagination,
} from "react-bootstrap";
import SaveButton from "../../../EMR_Buttons/SaveButton";
import * as Tostify from "../../../ConstUrl/Tostify";
import { ToastContainer } from "react-toastify";
import StaffModal from "./StaffModal";
import {
  loadAllStaffData,
  loadStaffData,
  loadUpdateStaffMember,
} from "../../../../redux/phcAction";
import { useDispatch, useSelector } from "react-redux";
import DisplayDataRow from "../../DisplayDataRow/DisplayDataRow";
import PageLoader from "../../../PageLoader";
import * as serviceHeaders from "../../../ConstUrl/serviceHeaders";
import { useHistory } from "react-router-dom";

export default function CreateStaff(props) {
  const removevitalstorege = () => {
    window.history.back();
  };
  let dispatch = useDispatch();
  const { staffData } = useSelector((state) => state.phcData);

  let phcuuid = sessionStorage.getItem("uuidofphc");

  const [displayData, setDisplayData] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = displayData.slice(indexOfFirstItem, indexOfLastItem);

  useEffect(() => {
    if (staffData) {
      let dataArray = [];
      staffData.forEach((element) => {
        if (element?.targetNode?.properties?.status == "ACTIVE") {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            photo: element?.targetNode?.properties?.photo,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: true,
            uuid: element?.targetNode?.properties?.uuid,
          });
        } else if (element?.targetNode?.properties?.status == "INACTIVE") {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            photo: element?.targetNode?.properties?.photo,
            code: element?.targetNode?.properties?.posted_by,
            staffRole: element?.targetNode?.properties?.type,
            status: false,
            uuid: element?.targetNode?.properties?.uuid,
          });
        } else {
          dataArray.push({
            salutation: element?.targetNode?.properties?.salutation,
            name: element?.targetNode?.properties?.name,
            photo: element?.targetNode?.properties?.photo,
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

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const handleNextPage = () => {
    if (currentPage < Math.ceil(displayData.length / itemsPerPage)) {
      setCurrentPage(currentPage + 1);
    }
  };
  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };
  const [addStaff, setAddStaff] = useState(false);
  const [staffEnable, setStaffEnable] = useState(false);
  const [staffShow, setStaffShow] = useState(false);

  const [staffMember, setStaffMember] = useState(false);

  const createNewStaff = () => setStaffShow(true);
  const updateExistStaff = () => setStaffShow(true);

  // const handleStaffClose = () => {
  //     setStaffShow(false);
  // }

  const removeStaff = (e) => {
    setAddStaff(false);
  };

  const handleStaff = () => {
    setAddStaff(true);
  };

  const [pageLoader, setPageLoader] = useState(false);
  const setStaffStatus = (e, i, uuid, staffRole) => {
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
      type: staffRole,
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
    dispatch(loadUpdateStaffMember(uuid, requestOptions, setPageLoader));
  };

  const [idForUpdate, setIdForUpdate] = useState(false);
  const updateStaff = (uuid) => {
    setIdForUpdate(uuid);
    dispatch(loadStaffData(uuid, setStaffShow));
    // setStaffShow(true);
  };

  useEffect(() => {
    if (phcuuid) {
      setPageLoader(true);
      dispatch(loadAllStaffData(phcuuid, setPageLoader));
    }
  }, [phcuuid]);

  let history = useHistory();
  const removeCurrentPage = () => {
    history.go(-2);
  };

  return (
    <React.Fragment>
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      <ToastContainer />
      {staffShow && (
        <StaffModal
          staffShow={staffShow}
          handleStaffClose={setStaffShow}
          setIdForUpdate={setIdForUpdate}
          setStaffMember={setStaffMember}
          idForUpdate={idForUpdate}
        />
      )}
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
                  Create Staff Members
                </Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="form-col">
              <Form className="super-admin-form">
                <div className="super-complaint">
                  <h3 className="super-config-details">Create Staff Members</h3>
                  <Row className="search-add-staff">
                    <Col lg={6} md={6}>
                      <div className="form-group sta-search">
                        <input
                          type="text"
                          className="form-control staff-search"
                          placeholder="Search Staff By Name / Contact Number"
                        />
                        <span className="fa fa-search form-control-pro-feedback d-flex"></span>
                      </div>
                    </Col>
                    <Col lg={6} md={6} className="create-phc">
                      <div className="save-btn-section">
                        <SaveButton
                          butttonClick={handleStaff}
                          class_name="regBtnN regStaff"
                          button_name="Add New Staff"
                        />
                      </div>
                    </Col>
                  </Row>
                </div>
                {/* {!displayData && !addStaff ? (
                  <div align="center">
                    <img
                      src="../img/super/create-fm.png"
                      className="staff-image"
                    />
                  </div>
                ) : (
                  <DisplayDataRow
                    displayData={displayData}
                    page="Create Staff"
                    setStatusEnable={setStaffStatus}
                    openModale={createNewStaff}
                    addNewRow={addStaff}
                    removeRow={removeStaff}
                    updateData={updateStaff}
                  />
                )} */}
                {currentItems.length > 0 ? (
                  <DisplayDataRow
                    displayData={currentItems}
                    page="Create Staff"
                    setStatusEnable={setStaffStatus}
                    openModale={createNewStaff}
                    addNewRow={addStaff}
                    removeRow={removeStaff}
                    updateData={updateStaff}
                  />
                ) : (
                  <div>No data to display.</div>
                )}

                {/* Pagination */}
                <div className="pagination-container">
                  <Pagination>
                    <Pagination.Prev onClick={handlePrevPage} />
                    {Array.from({
                      length: Math.ceil(displayData.length / itemsPerPage),
                    }).map((_, index) => (
                      <Pagination.Item
                        key={index}
                        active={index + 1 === currentPage}
                        onClick={() => handlePageChange(index + 1)}
                      >
                        {index + 1}
                      </Pagination.Item>
                    ))}
                    <Pagination.Next onClick={handleNextPage} />
                  </Pagination>
                </div>
              </Form>
            </div>
          </div>
        </div>
      </div>
    </React.Fragment>
  );
}
