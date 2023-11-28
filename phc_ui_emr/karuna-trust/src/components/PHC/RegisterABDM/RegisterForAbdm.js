import React, { useEffect, useState } from "react";
import { Form } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { loadSubCenters } from "../../../redux/actions";
import { loadAccessToken } from "../../../redux/AdminAction";
import {
  loadGetPhcDetails,
  loadSubCenterDetails,
} from "../../../redux/phcAction";
import PageLoader from "../../PageLoader";
import DisplayDataRow from "../DisplayDataRow/DisplayDataRow";
import RegisterModal from "./RegisterModal";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";

function RegisterForAbdm() {
  let dispatch = useDispatch();
  const phcUuid = sessionStorage.getItem("uuidofphc");
  const [pageLoader, setPageLoader] = useState(true);
  const { phcDetails } = useSelector((state) => state.phcData);
  const { subCentersList } = useSelector((state) => state.data);

  const [facilityType, setFacilityTypes] = useState([]);
  console.log(phcDetails);
  const facilityTypes = () => {
    var requestOptions = {
      method: "GET",
      headers: serviceHeaders.myHeaders1,
      redirect: "follow",
    };

    fetch(
      "https://dev-api-phcdt.sampoornaswaraj.org/abdm-svc/devservice/v1/bridges/getServices",
      requestOptions
    )
      .then((response) => response.json())
      .then((response) => {
        // console.log(response);

        // response.services.forEach((e) => {
        //   setFacilityTypes([...facilityType, e.id]);
        //   return facilityType;
        // });
        console.log(response.services);
        setFacilityTypes(response.services);

        return facilityType;
      })
      .catch((error) => console.log("error", error));
  };

  useEffect(() => {
    facilityTypes();
    dispatch(loadGetPhcDetails(phcUuid));
    dispatch(loadSubCenters(phcUuid, setPageLoader));
  }, [phcUuid, setPageLoader]);

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

  const [registerModalShow, setRegisterModalShow] = useState(false);

  const handleRegisterClose = () => {
    setRegisterModalShow(false);
  };

  const [centerType, setCenterType] = useState("");
  const handleRegisterOpen = () => {
    setCenterType("Phc");
    dispatch(loadAccessToken(setRegisterModalShow));
  };
  const handleSubRegisterOpen = (uuid) => {
    setCenterType("Sub_Center");
    dispatch(loadSubCenterDetails(uuid, setRegisterModalShow));
  };

  return (
    <div className="form-col">
      {/* loader */}
      {pageLoader == true && <PageLoader />}
      {/* loader */}
      {/* Open Register Modal */}
      {registerModalShow && (
        <RegisterModal
          displayData={displayData}
          registerModalShow={registerModalShow}
          centerType={centerType}
          facilityType={facilityType}
          handleRegisterClose={handleRegisterClose}
          setCenterType={setCenterType}
        />
      )}
      {/* Open Register Modal */}
      <Form className="super-admin-form">
        <div className="abdm-div">
          <h3 className="super-config-details">Register PHC for ABDM</h3>
          {/* <img src="../img/super/configphc.png" alt="configphc" /> */}
          <DisplayDataRow
            displayData={phcDetails}
            facilityType={facilityType}
            page="Sub-Center"
            type="register-abdm"
            data_type="object"
            handleRegisterOpen={handleRegisterOpen}
          />
        </div>
        <div>
          <h3 className="super-config-details">
            Register Sub-Centers/Health and Wellness Centers for ABDM
          </h3>
          {/* <img src="../img/super/configphc.png" alt="configphc" /> */}
          <DisplayDataRow
            displayData={displayData}
            facilityType={facilityType}
            page="Sub-Center"
            type="register-abdm"
            handleRegisterOpen={handleSubRegisterOpen}
          />
        </div>
      </Form>
    </div>
  );
}

export default RegisterForAbdm;
