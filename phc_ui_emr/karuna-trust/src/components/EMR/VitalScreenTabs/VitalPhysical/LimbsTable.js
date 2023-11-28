import React, { useState, useRef, useEffect } from "react";
import {
  Col,
  Row,
  Form,
  Button,
  OverlayTrigger,
  Tooltip,
  Accordion,
} from "react-bootstrap";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import PhyTableHeader from "./TableContent/PhyTableHeader";

function LimbsTable(props) {
  let phyExamItems = props.phyExamItems;
  let header = props.header;
  let Name_of_the_Joint = props.Name_of_Joint;
  // let type = props.table_type

  // for upper right limb
  let upperRightSholdLimbs = props.upperRightSholdLimbs;
  let upperRightElbLimbs = props.upperRightElbLimbs;
  let upperRightWriLimbs = props.upperRightWriLimbs;
  let upperRightHandLimbs = props.upperRightHandLimbs;
  // for upper right limb

  // for upper left limb
  let upperLeftSholdLimbs = props.upperLeftSholdLimbs;
  let upperLeftElbLimbs = props.upperLeftElbLimbs;
  let upperLeftWriLimbs = props.upperLeftWriLimbs;
  let upperLeftHandLimbs = props.upperLeftHandLimbs;
  // for upper left limb

  //for lower right limb
  let lowerRightHipLimbs = props.lowerRightHipLimbs;
  let lowerRightKneeLimbs = props.lowerRightKneeLimbs;
  let lowerRightAnkleLimbs = props.lowerRightAnkleLimbs;
  let lowerRightFootLimbs = props.lowerRightFootLimbs;
  //for lower right limb

  //for lower left limb
  let lowerLeftHipLimbs = props.lowerLeftHipLimbs;
  let lowerLeftKneeLimbs = props.lowerLeftKneeLimbs;
  let lowerLeftAnkleLimbs = props.lowerLeftAnkleLimbs;
  let lowerLeftFootLimbs = props.lowerLeftFootLimbs;
  //for lower left limb

  return (
    <div>
      <h2 className="physubtitle">{header}</h2>
      <div className="pro-field">
        <Paper>
          <TableContainer
            sx={{ maxHeight: 440 }}
            className="physical-limb-table"
          >
            <Table stickyHeader aria-label="sticky table">
              <PhyTableHeader />
              <TableBody>
                {Name_of_the_Joint.map((joint, i) => (
                  <TableRow key={i}>
                    <TableCell className="physical-tbody-name">
                      {joint.name}
                    </TableCell>
                    <TableCell className="physical-tbody-limb">
                      <div>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            className="table-drop-down"
                            value={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? upperRightSholdLimbs.ursMotionRange || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperRightElbLimbs.ureMotionRange || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperRightWriLimbs.urwMotionRange || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperRightHandLimbs.urhMotionRange || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? upperLeftSholdLimbs.ulsMotionRange || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperLeftElbLimbs.uleMotionRange || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperLeftWriLimbs.ulwMotionRange || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperLeftHandLimbs.ulhMotionRange || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerRightHipLimbs.lrhMotionRange || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerRightKneeLimbs.lrkMotionRange || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerRightAnkleLimbs.lraMotionRange || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? lowerRightFootLimbs.lrfMotionRange || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerLeftHipLimbs.llhMotionRange || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerLeftKneeLimbs.llkMotionRange || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerLeftAnkleLimbs.llaMotionRange || ""
                                : (header == "Left Lower Limb" &&
                                    joint.name == "Foot" &&
                                    lowerLeftFootLimbs.llfMotionRange) ||
                                  ""
                            }
                            name={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? "ursMotionRange"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "ureMotionRange"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "urwMotionRange"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? "urhMotionRange"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? "ulsMotionRange"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "uleMotionRange"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "ulwMotionRange"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? "ulhMotionRange"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? "lrhMotionRange"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? "lrkMotionRange"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "lraMotionRange"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? "lrfMotionRange"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? "llhMotionRange"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? "llkMotionRange"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "llaMotionRange"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  "llfMotionRange"
                            }
                            onChange={(e) => {
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? props.handleChangeURS(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeURE(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeURW(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeURH(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? props.handleChangeULS(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeULE(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeULW(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeULH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLRH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLRK(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLRA(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? props.handleChangeLRF(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLLH(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLLK(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLLA(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  props.handleChangeLLF(e);
                            }}
                          >
                            <option value="" hidden>
                              Select{" "}
                            </option>
                            {phyExamItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Range of Motion" && (
                                  <>
                                    {formItem.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.title}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </TableCell>
                    <TableCell className="physical-tbody-limb">
                      <div>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            className="table-drop-down"
                            value={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? upperRightSholdLimbs.ursStrength || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperRightElbLimbs.ureStrength || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperRightWriLimbs.urwStrength || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperRightHandLimbs.urhStrength || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? upperLeftSholdLimbs.ulsStrength || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperLeftElbLimbs.uleStrength || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperLeftWriLimbs.ulwStrength || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperLeftHandLimbs.ulhStrength || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerRightHipLimbs.lrhStrength || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerRightKneeLimbs.lrkStrength || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerRightAnkleLimbs.lraStrength || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? lowerRightFootLimbs.lrfStrength || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerLeftHipLimbs.llhStrength || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerLeftKneeLimbs.llkStrength || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerLeftAnkleLimbs.llaStrength || ""
                                : (header == "Left Lower Limb" &&
                                    joint.name == "Foot" &&
                                    lowerLeftFootLimbs.llfStrength) ||
                                  ""
                            }
                            name={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? "ursStrength"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "ureStrength"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "urwStrength"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? "urhStrength"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? "ulsStrength"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "uleStrength"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "ulwStrength"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? "ulhStrength"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? "lrhStrength"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? "lrkStrength"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "lraStrength"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? "lrfStrength"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? "llhStrength"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? "llkStrength"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "llaStrength"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  "llfStrength"
                            }
                            onChange={(e) =>
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? props.handleChangeURS(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeURE(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeURW(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeURH(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? props.handleChangeULS(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeULE(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeULW(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeULH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLRH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLRK(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLRA(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? props.handleChangeLRF(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLLH(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLLK(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLLA(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  props.handleChangeLLF(e)
                            }
                          >
                            <option value="" hidden>
                              Select{" "}
                            </option>
                            {phyExamItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Strength" && (
                                  <>
                                    {formItem.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.title}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </TableCell>
                    <TableCell className="physical-tbody-limb">
                      <div>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            className="table-drop-down"
                            value={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? upperRightSholdLimbs.ursWasting || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperRightElbLimbs.ureWasting || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperRightWriLimbs.urwWasting || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperRightHandLimbs.urhWasting || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? upperLeftSholdLimbs.ulsWasting || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperLeftElbLimbs.uleWasting || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperLeftWriLimbs.ulwWasting || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperLeftHandLimbs.ulhWasting || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerRightHipLimbs.lrhWasting || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerRightKneeLimbs.lrkWasting || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerRightAnkleLimbs.lraWasting || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? lowerRightFootLimbs.lrfWasting || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerLeftHipLimbs.llhWasting || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerLeftKneeLimbs.llkWasting || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerLeftAnkleLimbs.llaWasting || ""
                                : (header == "Left Lower Limb" &&
                                    joint.name == "Foot" &&
                                    lowerLeftFootLimbs.llfWasting) ||
                                  ""
                            }
                            name={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? "ursWasting"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "ureWasting"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "urwWasting"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? "urhWasting"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? "ulsWasting"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "uleWasting"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "ulwWasting"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? "ulhWasting"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? "lrhWasting"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? "lrkWasting"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "lraWasting"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? "lrfWasting"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? "llhWasting"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? "llkWasting"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "llaWasting"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  "llfWasting"
                            }
                            onChange={(e) =>
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? props.handleChangeURS(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeURE(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeURW(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeURH(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? props.handleChangeULS(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeULE(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeULW(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeULH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLRH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLRK(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLRA(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? props.handleChangeLRF(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLLH(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLLK(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLLA(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  props.handleChangeLLF(e)
                            }
                          >
                            <option value="" hidden>
                              Select{" "}
                            </option>
                            {phyExamItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Wasting" && (
                                  <>
                                    {formItem.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.title}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </TableCell>
                    <TableCell className="physical-tbody-limb">
                      <div>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            className="table-drop-down"
                            value={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? upperRightSholdLimbs.ursSensation || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperRightElbLimbs.ureSensation || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperRightWriLimbs.urwSensation || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperRightHandLimbs.urhSensation || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? upperLeftSholdLimbs.ulsSensation || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperLeftElbLimbs.uleSensation || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperLeftWriLimbs.ulwSensation || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperLeftHandLimbs.ulhSensation || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerRightHipLimbs.lrhSensation || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerRightKneeLimbs.lrkSensation || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerRightAnkleLimbs.lraSensation || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? lowerRightFootLimbs.lrfSensation || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerLeftHipLimbs.llhSensation || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerLeftKneeLimbs.llkSensation || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerLeftAnkleLimbs.llaSensation || ""
                                : (header == "Left Lower Limb" &&
                                    joint.name == "Foot" &&
                                    lowerLeftFootLimbs.llfSensation) ||
                                  ""
                            }
                            name={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? "ursSensation"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "ureSensation"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "urwSensation"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? "urhSensation"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? "ulsSensation"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "uleSensation"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "ulwSensation"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? "ulhSensation"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? "lrhSensation"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? "lrkSensation"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "lraSensation"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? "lrfSensation"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? "llhSensation"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? "llkSensation"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "llaSensation"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  "llfSensation"
                            }
                            onChange={(e) =>
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? props.handleChangeURS(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeURE(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeURW(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeURH(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? props.handleChangeULS(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeULE(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeULW(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeULH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLRH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLRK(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLRA(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? props.handleChangeLRF(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLLH(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLLK(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLLA(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  props.handleChangeLLF(e)
                            }
                          >
                            <option value="" hidden>
                              Select{" "}
                            </option>
                            {phyExamItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Sensation" && (
                                  <>
                                    {formItem.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.title}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </TableCell>
                    <TableCell className="physical-tbody-limb">
                      <div>
                        <Form.Group
                          className="mb-3_fname"
                          controlId="exampleForm.FName"
                        >
                          <Form.Select
                            aria-label="Default select example"
                            className="table-drop-down"
                            value={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? upperRightSholdLimbs.ursReflexes || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperRightElbLimbs.ureReflexes || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperRightWriLimbs.urwReflexes || ""
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperRightHandLimbs.urhReflexes || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? upperLeftSholdLimbs.ulsReflexes || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? upperLeftElbLimbs.uleReflexes || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? upperLeftWriLimbs.ulwReflexes || ""
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? upperLeftHandLimbs.ulhReflexes || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerRightHipLimbs.lrhReflexes || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerRightKneeLimbs.lrkReflexes || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerRightAnkleLimbs.lraReflexes || ""
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? lowerRightFootLimbs.lrfReflexes || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? lowerLeftHipLimbs.llhReflexes || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? lowerLeftKneeLimbs.llkReflexes || ""
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? lowerLeftAnkleLimbs.llaReflexes || ""
                                : (header == "Left Lower Limb" &&
                                    joint.name == "Foot" &&
                                    lowerLeftFootLimbs.llfReflexes) ||
                                  ""
                            }
                            name={
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? "ursReflexes"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "ureReflexes"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "urwReflexes"
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? "urhReflexes"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? "ulsReflexes"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? "uleReflexes"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? "ulwReflexes"
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? "ulhReflexes"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? "lrhReflexes"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? "lrkReflexes"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "lraReflexes"
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? "lrfReflexes"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? "llhReflexes"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? "llkReflexes"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? "llaReflexes"
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  "llfReflexes"
                            }
                            onChange={(e) =>
                              header == "Right Upper Limb" &&
                              joint.name == "Shoulder"
                                ? props.handleChangeURS(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeURE(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeURW(e)
                                : header == "Right Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeURH(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Shoulder"
                                ? props.handleChangeULS(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Elbow"
                                ? props.handleChangeULE(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Wrist"
                                ? props.handleChangeULW(e)
                                : header == "Left Upper Limb" &&
                                  joint.name == "Hand"
                                ? props.handleChangeULH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLRH(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLRK(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLRA(e)
                                : header == "Right Lower Limb" &&
                                  joint.name == "Foot"
                                ? props.handleChangeLRF(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Hip"
                                ? props.handleChangeLLH(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Knee"
                                ? props.handleChangeLLK(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Ankle"
                                ? props.handleChangeLLA(e)
                                : header == "Left Lower Limb" &&
                                  joint.name == "Foot" &&
                                  props.handleChangeLLF(e)
                            }
                          >
                            <option value="" hidden>
                              Select{" "}
                            </option>
                            {phyExamItems.map((formItem, i) => (
                              <React.Fragment key={i}>
                                {formItem.groupName == "Reflexes" && (
                                  <>
                                    {formItem.elements.map(
                                      (drpItem, drpIndex) => (
                                        <option
                                          value={drpItem.title}
                                          key={drpIndex}
                                        >
                                          {drpItem.title}
                                        </option>
                                      )
                                    )}
                                  </>
                                )}
                              </React.Fragment>
                            ))}
                          </Form.Select>
                        </Form.Group>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>
      </div>
    </div>
  );
}

export default LimbsTable;
