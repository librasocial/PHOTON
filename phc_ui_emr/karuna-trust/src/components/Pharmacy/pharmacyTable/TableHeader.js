import React from "react";
import { TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import SupplierName from "../Order/Filter/SupplierName";
import OrderType from "../Order/Filter/OrderType";
import StatusFilter from "../Order/Filter/StatusFilter";

function TableHeader(props) {
  let typeofuser = sessionStorage.getItem("typeofuser");

  // onclick event for product page
  const ApphabeticByGroup = () => {
    props.setAlphaState(true);
    props.setUnAlphaState(false);
    props.setAlphaStateClass(false);
    props.setAlphaStateName(false);
    props.setUnAlphaStateClass(false);
    props.setUnAlphaStateName(false);
  };
  const UnApphabeticByGroup = () => {
    props.setUnAlphaState(true);
    props.setAlphaState(false);
    props.setAlphaStateClass(false);
    props.setAlphaStateName(false);
    props.setUnAlphaStateClass(false);
    props.setUnAlphaStateName(false);
  };

  const ApphabeticByClass = () => {
    props.setUnAlphaState(false);
    props.setAlphaState(false);
    props.setAlphaStateClass(true);
    props.setAlphaStateName(false);
    props.setUnAlphaStateClass(false);
    props.setUnAlphaStateName(false);
  };
  const UnApphabeticByClass = () => {
    props.setUnAlphaState(false);
    props.setAlphaState(false);
    props.setAlphaStateClass(false);
    props.setAlphaStateName(false);
    props.setUnAlphaStateClass(true);
    props.setUnAlphaStateName(false);
  };

  const ApphabeticByName = () => {
    props.setUnAlphaState(false);
    props.setAlphaState(false);
    props.setAlphaStateClass(false);
    props.setAlphaStateName(true);
    props.setUnAlphaStateClass(false);
    props.setUnAlphaStateName(false);
  };
  const UnApphabeticByName = () => {
    props.setUnAlphaState(false);
    props.setAlphaState(false);
    props.setAlphaStateClass(false);
    props.setAlphaStateName(false);
    props.setUnAlphaStateClass(false);
    props.setUnAlphaStateName(true);
  };
  // onclick event for product page
  return (
    <React.Fragment>
      <TableHead>
        <TableRow>
          {props.tableHeader.map((items, i) => (
            <React.Fragment key={i}>
              {typeofuser === "Pharmacist" ? (
                <>
                  {props.pageType === "Pharma-Products" ? (
                    <TableCell
                      key={i}
                      align={
                        items.name == "S.I. No." || items.name == "Action"
                          ? "center"
                          : "left"
                      }
                      className={
                        items.class == "class1"
                          ? "theader-1"
                          : items.class == "class2"
                          ? "theader-2"
                          : items.class == "class3"
                          ? "theader-3"
                          : items.class == "class4"
                          ? "theader-4"
                          : ""
                      }
                    >
                      {items.type === "group" ? (
                        <>
                          {items.name}&nbsp;&nbsp;
                          <span className="prod-Icons">
                            {items.name === "Product Group" && (
                              <>
                                {props.alphaState == true ? (
                                  <div
                                    style={{ height: "16px" }}
                                    onClick={UnApphabeticByGroup}
                                  >
                                    <i className="bi bi-caret-down-fill pro-icon pro-down"></i>
                                  </div>
                                ) : (
                                  <div
                                    style={{ height: "16px" }}
                                    onClick={ApphabeticByGroup}
                                  >
                                    <i className="bi bi-caret-up-fill  pro-up"></i>
                                  </div>
                                )}
                              </>
                            )}
                            {items.name === "Classification" && (
                              <>
                                {props.alphaStateClass == true ? (
                                  <div
                                    style={{ height: "16px" }}
                                    onClick={UnApphabeticByClass}
                                  >
                                    <i className="bi bi-caret-down-fill pro-icon pro-down"></i>
                                  </div>
                                ) : (
                                  <div
                                    style={{ height: "16px" }}
                                    onClick={ApphabeticByClass}
                                  >
                                    <i className="bi bi-caret-up-fill  pro-up"></i>
                                  </div>
                                )}
                              </>
                            )}
                            {items.name === "Product Name" && (
                              <>
                                {props.alphaStateName == true ? (
                                  <div
                                    style={{ height: "16px" }}
                                    onClick={UnApphabeticByName}
                                  >
                                    <i className="bi bi-caret-down-fill pro-icon pro-down"></i>
                                  </div>
                                ) : (
                                  <div
                                    style={{ height: "16px" }}
                                    onClick={ApphabeticByName}
                                  >
                                    <i className="bi bi-caret-up-fill  pro-up"></i>
                                  </div>
                                )}
                              </>
                            )}
                          </span>
                        </>
                      ) : (
                        <>{items.name}</>
                      )}
                    </TableCell>
                  ) : (
                    <TableCell
                      align={
                        items.name == "S.I. No." || items.name == "Action"
                          ? "center"
                          : "left"
                      }
                      className={
                        items.class == "class1"
                          ? "theader-1"
                          : items.class == "class2"
                          ? "theader-2"
                          : items.class == "class3"
                          ? "theader-3"
                          : items.class == "class4"
                          ? "theader-4"
                          : items.class == "class5"
                          ? "theader-5"
                          : ""
                      }
                    >
                      {items.type === "group" ? (
                        <div className="supplier-filter">
                          {items.name}{" "}
                          <>
                            {items.name === "Supplier Name" ? (
                              <SupplierName
                                items={props.supplierItem}
                                setSuplierName={props.setSuplierName}
                                suplierName={props.suplierName}
                              />
                            ) : items.name === "Order Type" ||
                              items.name === "Inwards Type" ? (
                              <OrderType
                                items1={props.supplierItem1}
                                setSelectedType={props.setSelectedType}
                                selectedType={props.selectedType}
                              />
                            ) : items.name === "Status" ||
                              items.name === "Inwards Status" ? (
                              <StatusFilter
                                items2={props.supplierItem2}
                                setStatusValue={props.setStatusValue}
                                statusValue={props.statusValue}
                              />
                            ) : (
                              ""
                            )}
                          </>
                        </div>
                      ) : (
                        <>{items.name}</>
                      )}
                    </TableCell>
                  )}
                </>
              ) : (
                <TableCell
                  key={i}
                  align={
                    items.name == "S.I. No." || items.name == "Action"
                      ? "center"
                      : "left"
                  }
                  className={
                    items.class == "class1"
                      ? "theader-1"
                      : items.class == "class2"
                      ? "theader-2"
                      : items.class == "class3"
                      ? "theader-3"
                      : items.class == "class4"
                      ? "theader-4"
                      : ""
                  }
                >
                  {items.name}
                </TableCell>
              )}
            </React.Fragment>
          ))}
        </TableRow>
      </TableHead>
    </React.Fragment>
  );
}

export default TableHeader;
