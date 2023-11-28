import React, { useState, useRef, useEffect } from "react";
import { Col, Row, Modal } from "react-bootstrap";
import SaveButton from "../../EMR_Buttons/SaveButton";
import ChangingProgressProvider from "./ChangingProgressProvider";
import {
  CircularProgressbar,
  buildStyles,
  CircularProgressbarWithChildren,
} from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";
import Webcam from "react-webcam";
import { Camera, FACING_MODES, IMAGE_TYPES } from "react-html5-camera-photo";
import * as serviceHeaders from "../../ConstUrl/serviceHeaders";
import "./FileUpload.css";
import { useDispatch } from "react-redux";
import { loadUpdatePHC, loadUploadImage } from "../../../redux/phcAction";

export default function FileUpload(props) {
  let dispatch = useDispatch();
  let imageShow = props.imageShow;
  let imgText = props.imgText;
  let upload = props.upload;

  const [image, setImage] = useState({ preview: "", raw: "" });
  const [baseUrl, setBaseUrl] = useState(null);

  const getBase64 = (file) => {
    return new Promise((resolve) => {
      let fileInfo;
      let baseURL = "";
      // Make new FileReader
      let reader = new FileReader();
      // Convert the file to base64 text
      reader.readAsDataURL(file);

      // on reader load somthing...
      reader.onload = () => {
        // Make a fileInfo Object
        baseURL = reader.result;
        resolve(baseURL);
      };
    });
  };

  const [imagefile, setImagefile] = useState("");

  const handleChange = (e) => {
    if (e.target.files.length) {
      setImage({
        preview: URL.createObjectURL(e.target.files[0]),
        raw: e.target.files[0],
      });

      //   converting base64
      let file = e.target.files[0];
      setImagefile(file);
      getBase64(file)
        .then((result) => {
          file["base64"] = result;
          setBaseUrl(result);
        })
        .catch((err) => {});

      setBaseUrl({
        file: e.target.files[0],
      });
      //   converting base64
    }
  };

  const videoElement = useRef(null);

  const [imgSrc, setImgSrc] = useState(null);
  const [isShowVideo, setIsShowVideo] = useState(false);

  const captureImage = () => {
    setIsShowVideo(true);
  };

  const capture = React.useCallback(async () => {
    const imageSrc = videoElement.current.getScreenshot();
    function dataURLtoFile(dataurl, filename) {
      var arr = dataurl.split(","),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], filename, { type: mime });
    }
    //     //Usage example:
    var file = dataURLtoFile(imageSrc, "hello.jpeg");

    setImagefile(file);
    setImgSrc(imageSrc);
    setBaseUrl(imageSrc);
    // setIsShowVideo(true);
  }, [videoElement, setImgSrc, setBaseUrl]);

  // code for retake photo
  const Retake = () => {
    setImgSrc("");
    setImagefile("");
  };

  const closeModal = () => {
    props.handleImageClose(false);
    if (props.center_type == "Phc") {
      props.setEditUuid("");
    }
  };
  const [pageLoader, setPageLoader] = useState(false);

  const uploadImage = () => {
    if (props.center_type == "Sub-Center" || props.center_type == "Staff") {
      props.setUploadImage(baseUrl);
      closeModal();
    } else if (props.center_type == "Phc") {
      let updateData = {
        type: "Phc",
        properties: {
          // "photo": baseUrl
          photo: props.phcData.facilityId + ".jpeg",
        },
      };
      // console.log(imagefile, "updateData")
      var postResponse = {
        headers: serviceHeaders.myHeaders1,
        method: "PATCH",
        mode: "cors",
        body: JSON.stringify(updateData),
      };
      let authtoken5 = sessionStorage.getItem("token");
      let myHeaders5 = new Headers();
      let jwttoken5 = "Bearer " + "" + authtoken5;
      myHeaders5.append("Content-Type", "multipart/form-data");
      myHeaders5.append("Access-Control-Allow-Origin", "*");

      var requestOptionsimagedata = {
        headers: myHeaders5,
        method: "PUT",
        mode: "cors",
        body: imagefile,
      };
      console.log(imagefile, "imagefile");
      dispatch(
        loadUploadImage(props.phcData.facilityId, requestOptionsimagedata)
      );
      dispatch(
        loadUpdatePHC(props.editUuid, postResponse, setPageLoader, closeModal)
      );
    }
  };

  const closeImgModal = () => {
    setImage({ preview: "", raw: "" });
    setImgSrc("");
    setImagefile("");
    setIsShowVideo(false);
    setBaseUrl(null);
    props.handleImageClose(false);
    props.setImageChange(false);
  };

  const captureModal = () => {
    props.setImageChange(false);
  };

  const uploadModal = () => {
    props.setImageChange(false);
    props.setImgText(true);
  };

  return (
    <React.Fragment>
      <Modal
        show={imageShow}
        onHide={props.handleImageClose}
        className="file-upload-div"
      >
        {imgText == true && props.imageChange == false ? (
          <React.Fragment>
            <div className="file-header-div">
              <h5 className="file-process-header">
                Upload PHC photo from Computer
                <i
                  className="fa fa-close close-btn-style"
                  onClick={closeImgModal}
                ></i>
              </h5>
            </div>
            <div className="image-upload-div">
              <div>
                {image.preview ? (
                  <img
                    src={image.preview}
                    alt="dummy"
                    className="user-uploaded-img"
                  />
                ) : (
                  <div className="browse-div">
                    <img
                      src="../img/super/facility.png"
                      className="user-login-img"
                    />
                    <p className="user-login-text">
                      Drag and Drop Files <br /> Or <br />
                    </p>
                    <div className="browse-div">
                      <label htmlFor="upload-button" className="regBtnBrowse">
                        Select Image
                      </label>
                      <input
                        type="file"
                        id="upload-button"
                        style={{ display: "none" }}
                        onChange={handleChange}
                      />
                    </div>
                  </div>
                )}
              </div>
              {/* </>
                            } */}
            </div>
            <Row>
              <Col className="btn1">
                <div className="save-btn-section">
                  <SaveButton
                    class_name="regBtnPC"
                    butttonClick={closeImgModal}
                    button_name="Cancel"
                  />
                </div>
              </Col>
              <Col className="btn2">
                <div className="save-btn-section">
                  <SaveButton
                    class_name="regBtnN"
                    button_name="Finish"
                    butttonClick={uploadImage}
                  />
                </div>
              </Col>
            </Row>
          </React.Fragment>
        ) : (
          <>
            {props.imageChange == false && (
              <React.Fragment>
                <div className="file-header-div">
                  <h5 className="file-process-header">
                    Capture Image
                    <i
                      className="fa fa-close close-btn-style"
                      onClick={closeImgModal}
                    ></i>
                  </h5>
                </div>
                <div className="image-upload-div">
                  {isShowVideo ? (
                    <>
                      {!imgSrc ? (
                        <Webcam
                          style={{
                            height: "250px",
                            width: "100%",
                          }}
                          audio={false}
                          ref={videoElement}
                          screenshotFormat="image/jpeg"
                        />
                      ) : (
                        <>
                          <img
                            src={imgSrc}
                            style={{
                              height: "250px",
                              width: "100%",
                            }}
                          />
                        </>
                      )}
                    </>
                  ) : (
                    <img
                      src="../img/super/facility.png"
                      className="user-login-img"
                    />
                  )}
                </div>
                <Row className="image-uploaded">
                  <Col>
                    <div className="save-btn-section">
                      <SaveButton
                        class_name="regBtnPC"
                        butttonClick={closeImgModal}
                        button_name="Cancel"
                      />
                    </div>
                  </Col>
                  <Col>
                    <Row className="image-capture-div">
                      <Col className="image-capture-div" lg="8">
                        <div className="save-btn-section">
                          {!isShowVideo && (
                            <SaveButton
                              class_name="regBtnPCImg"
                              butttonClick={captureImage}
                              button_name="Capture"
                            />
                          )}
                          {isShowVideo && (
                            <>
                              {imagefile ? (
                                <SaveButton
                                  class_name="regBtnPCImg"
                                  butttonClick={Retake}
                                  button_name="Retake"
                                />
                              ) : (
                                <SaveButton
                                  class_name="regBtnPCImg"
                                  butttonClick={capture}
                                  button_name="Capture"
                                />
                              )}
                            </>
                          )}
                        </div>
                      </Col>
                      <Col lg="4">
                        <div className="save-btn-section">
                          <SaveButton
                            class_name="regBtnN"
                            button_name="Finish"
                            butttonClick={uploadImage}
                          />
                        </div>
                      </Col>
                    </Row>
                  </Col>
                </Row>
              </React.Fragment>
            )}
          </>
        )}
        {props.imageChange == true && (
          <React.Fragment>
            <div className="file-header-div">
              <h5 className="file-process-header">
                Capture Image
                <i
                  className="fa fa-close close-btn-style"
                  onClick={closeImgModal}
                ></i>
              </h5>
            </div>
            <div className="image-upload-div">
              <div style={{ width: "80%" }}>
                <img
                  src="../img/super/facility.png"
                  className="user-login-img"
                />
                <Row className="image-uploaded">
                  <Col align="center">
                    <SaveButton
                      class_name="regBtnPCImg"
                      button_name="Capture"
                      butttonClick={captureModal}
                    />
                  </Col>
                  <Col align="center">
                    <SaveButton
                      class_name="regBtnPCImg"
                      button_name="Upload"
                      butttonClick={uploadModal}
                    />
                  </Col>
                </Row>

                {/* <div>
                                            <p style={{ marginTop: "6px" }}>
                                                or
                                            </p>
                                            <p className="uploadphc"  style={{cursor:"pointer"}}>
                                                Upload from Computer
                                            </p>
                                        </div> */}
              </div>
            </div>
          </React.Fragment>
        )}
      </Modal>
    </React.Fragment>
  );
}
