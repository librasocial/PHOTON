import React from "react";
import { Button } from "react-bootstrap";

function Loader(props) {
  function loderCancel() {
    sessionStorage.removeItem("LabResid");
    sessionStorage.removeItem("LabOrdId");
    sessionStorage.removeItem("LabPateintid");
    props.setInputList([
      {
        productId: "",
        productName: "",
        batchNo: "",
        expiryDate: "",
        orderQty: "",
        qtyIssued: "",
      },
    ]);
    props.setLoading1(false);
  }

  const closeModal = props.closeLoader;
  const closeIndentModal = props.closeIndentLoader;
  const closePurchaseModal = props.closePurchaseLoader;
  const closeInwardModal = props.closeInwardLoader;
  const closeApprovalModal = props.approvalLoadingClose;

  return (
    <>
      {props.isLoading1 == true && (
        <div className="loder-container" align="center">
          <div className="loadBox" align="center">
            <div>
              <img
                className="thumbnail pharmacy-loader-img"
                src="../../Images/005.png"
              />
            </div>
            <div>
              <h1 className="loader-head">Order dispensed successfully.</h1>
            </div>
            <div>
              <h1 className="loader-order-id">Order Id : # ORD0012345</h1>
            </div>
            <Button className="okay-btn btn btn-primary" onClick={loderCancel}>
              Okay
            </Button>
          </div>
        </div>
      )}
      {props.isNewLoading == true && (
        <div className="loder-container" align="center">
          <div className="loader-product" align="center">
            <div className="d-flex justify-content-center align-items-center">
              <img src="../img/Pharmacy/success-icon.svg" alt="success-img" />
            </div>
            <div className="p-2">
              <h6 className="">
                <b>
                  {props.productId
                    ? "Product Updated Successfully"
                    : "Product Added Successfully"}
                </b>
              </h6>
            </div>
            <div>
              <Button
                variant="outline-secondary"
                className="loadBtn pr-5 pl-5 pt-2 pb-2"
                onClick={closeModal}
              >
                Okay
              </Button>
            </div>
          </div>
        </div>
      )}
      {props.purchaseLoading == true && (
        <div className="loder-container" align="center">
          <div className="loader-product" align="center">
            <div className="d-flex justify-content-center align-items-center">
              <img src="../img/Pharmacy/order-success.svg" alt="success-img" />
            </div>
            <div className="p-2">
              <h6 className="">
                <b>
                  {props.orderId ? (
                    <>Purchase order updated as {props.status}</>
                  ) : (
                    <>Purchase order saved as {props.status}</>
                  )}
                </b>
              </h6>
            </div>
            <div>
              <Button
                variant="outline-secondary"
                className="loadBtn pr-5 pl-5 pt-2 pb-2"
                onClick={closePurchaseModal}
              >
                Okay
              </Button>
            </div>
          </div>
        </div>
      )}
      {props.indentLoading == true && (
        <div className="loder-container" align="center">
          <div className="loader-product" align="center">
            <div className="d-flex justify-content-center align-items-center">
              <img src="../img/Pharmacy/order-success.svg" alt="success-img" />
            </div>
            <div className="p-2">
              <h6 className="">
                <b>
                  {props.orderId ? (
                    <>Indent updated as {props.status}</>
                  ) : (
                    <>Indent saved as {props.status}</>
                  )}
                </b>
              </h6>
            </div>
            <div>
              <Button
                variant="outline-secondary"
                className="loadBtn pr-5 pl-5 pt-2 pb-2"
                onClick={closeIndentModal}
              >
                Okay
              </Button>
            </div>
          </div>
        </div>
      )}
      {props.inwardsLoading1 == true && (
        <div className="loder-container" align="center">
          <div className="loader-product" align="center">
            <div className="p-2">
              <h2 className="">
                <b>
                  {props.inwardsId
                    ? "Updating inwards record"
                    : "Creating inwards record"}
                </b>
              </h2>
            </div>
            <div className="d-flex justify-content-center align-items-center">
              <img
                src="../img/Pharmacy/inwards-circle-1.svg"
                alt="success-img"
              />
            </div>
            <div className="p-2">
              <h6 className="">
                {props.inwardsId
                  ? "Please wait while we update new Inwards into records"
                  : "Please wait while we create new Inwards into records"}
              </h6>
            </div>
          </div>
        </div>
      )}
      {props.inwardsLoading2 == true && (
        <div className="loder-container" align="center">
          <div className="loader-product" align="center">
            <div className="p-2">
              <h2 className="">
                <b>
                  {props.inwardsId
                    ? "Updating inwards record"
                    : "Creating inwards record"}
                </b>
              </h2>
            </div>
            <div className="d-flex justify-content-center align-items-center">
              <img
                src="../img/Pharmacy/inwards-circle-2.svg"
                alt="success-img"
              />
            </div>
            <div className="p-2">
              <h6 className="">
                <b>
                  {props.inwardsId
                    ? "Please wait while we update new Inwards into records"
                    : "Please wait while we create new Inwards into records"}
                </b>
              </h6>
            </div>
          </div>
        </div>
      )}
      {props.inwardsLoading3 == true && (
        <div className="loder-container" align="center">
          <div className="loader-product" align="center">
            <div className="d-flex justify-content-center align-items-center">
              <img src="../img/Pharmacy/order-success.svg" alt="success-img" />
            </div>
            <div className="p-2">
              <h6 className="">
                <b>
                  {props.inwardsId
                    ? "Inwards updated succesfully"
                    : "Inwards created succesfully"}
                </b>
              </h6>
            </div>
            <hr />
            <div>
              <h1 className="loader-order-id">ORD0012345</h1>
            </div>
            <div>
              <Button
                variant="outline-secondary"
                className="loadBtn pr-5 pl-5 pt-2 pb-2"
                onClick={closeInwardModal}
              >
                Okay
              </Button>
            </div>
          </div>
        </div>
      )}

      {props.approvalLoading == true && (
        <div className="loder-container" align="center">
          <div className="loader-product" align="center">
            <div className="d-flex justify-content-center align-items-center">
              <img src="../img/thumbs-up-icon.svg" alt="success-img" />
            </div>
            <div className="p-2">
              <h6 className="">
                <b>Purchase order authorised successfully.</b>
              </h6>
            </div>
            <hr />
            <div>
              <h1 className="loader-order-id">PO Number : # PO-00012345</h1>
            </div>
            <div>
              <Button
                variant="outline-secondary"
                className="loadBtn pr-5 pl-5 pt-2 pb-2"
                onClick={closeApprovalModal}
              >
                Okay
              </Button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

export default Loader;
