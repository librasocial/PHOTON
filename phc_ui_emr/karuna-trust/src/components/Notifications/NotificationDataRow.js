import React from "react";
import { Col, Row } from "react-bootstrap";
import SignedHealthIDImage from "../Dashboard/SignedHealthIDImage";
import { Link } from "react-router-dom";
import moment from "moment";
import { useHistory } from "react-router-dom";

function NotificationDataRow(props) {
  let history = useHistory();
  const dateToday = new Date();
  const todayDate = moment(dateToday).format("YYYY-MM-DD");
  let yesterdayDate = moment(dateToday.setDate(dateToday.getDate() - 1)).format(
    "YYYY-MM-DD"
  );
  const currentMonth = moment().format("MMMM");

  const displaydays = (day1, day2) => {
    const start = moment(day2, "YYYY-MM-DD");
    const end = moment(day1, "YYYY-MM-DD");
    const diff = end.diff(start, "days");
    return diff;
  };

  const displayHours = (time1, time2) => {
    var startTime = moment(time2, "HH:mm:ss a");
    var endTime = moment(time1, "HH:mm:ss a");
    var duration = moment.duration(endTime.diff(startTime));
    var hours = parseInt(duration.asHours());
    return hours;
  };
  const displayMinutes = (time1, time2) => {
    var startTime = moment(time2, "HH:mm:ss a");
    var endTime = moment(time1, "HH:mm:ss a");
    var duration = moment.duration(endTime.diff(startTime));
    var hours = parseInt(duration.asHours());
    var minutes = parseInt(duration.asMinutes()) - hours * 60;
    return minutes;
  };

  //start consultation
  const consultation_page = (e) => {
    sessionStorage.setItem("LabPateintid", e);
    sessionStorage.setItem("vitalon", "on");
    history.push("/vitals");
  };
  //start consultation

  // report page
  const mopostatus = (id, pat_id) => {
    sessionStorage.setItem("poUser", "medical-Officer");
    sessionStorage.setItem("LabOrderId", id);
    sessionStorage.setItem("LabPateintid", pat_id);
  };
  const orderPoApprovels = (poid) => {
    sessionStorage.setItem("poUser", "medical-Officer");
    sessionStorage.setItem("poID", poid);
  };
  const orderInApprovels = (inid) => {
    sessionStorage.setItem("InUser", "medical-Officer");
    sessionStorage.setItem("inID", inid);
  };
  // report page

  return (
    <div>
      <div>
        {Object.keys(props.byDate).map((date, i) => (
          <div key={i} className="pro-div-row">
            <div className="approval-row-date">
              <p className="approval-date">
                {date == todayDate ? (
                  "Today"
                ) : date == yesterdayDate ? (
                  "Yesterday"
                ) : (
                  <>{moment(date).format("DD MMM YYYY")}</>
                )}
                {/* ({moment(date).format("DD MMM YYYY")}) */}
              </p>
            </div>
            {props.byDate[date].map((notifyData, i) => (
              <div className="search-pro-div flex-column" key={i}>
                <Row className="searchFilter-row">
                  <Col
                    md={1}
                    align="center"
                    className="d-flex justify-content-center"
                  >
                    <SignedHealthIDImage
                      healthID={notifyData.healthId && notifyData.healthId}
                    />
                  </Col>
                  <Col md={8} className="text-left">
                    {notifyData.name && (
                      <p className="approval-text">
                        {/* Investigation report of <span className="approvalslink" onClick={(e) => consultation_page(notifyData.pat_id)} >{notifyData.name}</span> is ready. <br /> */}
                        Investigation report of{" "}
                        <span>
                          <b>{notifyData.name}</b>
                        </span>{" "}
                        is ready. <br />
                        Order Id:&nbsp;{" "}
                        <Link
                          className="approvalslink"
                          to="/Report"
                          onClick={(e) =>
                            mopostatus(notifyData.id, notifyData.pat_id)
                          }
                        >
                          {notifyData.id}
                        </Link>
                      </p>
                    )}
                    {notifyData.raisedby && (
                      <p className="approval-text">
                        {notifyData.poDetails
                          ? "Purchase"
                          : notifyData.indentItems && "Indent"}{" "}
                        Order raised by <b>{notifyData.raisedby}</b>. <br />
                        Order Id:{" "}
                        {notifyData.poDetails ? (
                          <Link
                            className="approvalslink"
                            to="/poApprovals"
                            onClick={(e) => orderPoApprovels(notifyData.id)}
                          >
                            {" "}
                            PO-{notifyData.id}{" "}
                          </Link>
                        ) : (
                          <>
                            {notifyData.indentItems && (
                              <Link
                                className="approvalslink"
                                to="/poApprovals"
                                onClick={(e) => orderInApprovels(notifyData.id)}
                              >
                                {" "}
                                IND-{notifyData.id}{" "}
                              </Link>
                            )}
                          </>
                        )}
                      </p>
                    )}
                  </Col>
                  <Col md={3} className="text-right">
                    {date == todayDate ? (
                      <p className="approval-time">
                        {displayHours(
                          moment(dateToday).format("hh:mm:ss A"),
                          moment(notifyData.date).format("hh:mm:ss A")
                        ) == 0 &&
                        displayMinutes(
                          moment(dateToday).format("hh:mm:ss A"),
                          moment(notifyData.date).format("hh:mm:ss A")
                        ) < 59 ? (
                          <>
                            {displayMinutes(
                              moment(dateToday).format("hh:mm:ss A"),
                              moment(notifyData.date).format("hh:mm:ss A")
                            )}{" "}
                            &nbsp;minutes ago{" "}
                          </>
                        ) : (
                          <>
                            {displayHours(
                              moment(dateToday).format("hh:mm:ss A"),
                              moment(notifyData.date).format("hh:mm:ss A")
                            )}{" "}
                            &nbsp;hrs&nbsp;
                            {displayMinutes(
                              moment(dateToday).format("hh:mm:ss A"),
                              moment(notifyData.date).format("hh:mm:ss A")
                            )}{" "}
                            &nbsp;minutes ago
                          </>
                        )}
                      </p>
                    ) : (
                      <p className="approval-time">
                        {moment(date).format("MMMM") == currentMonth ? (
                          <>
                            {displaydays(
                              todayDate,
                              moment(notifyData.date).format("YYYY-MM-DD")
                            )}{" "}
                            &nbsp;days ago{" "}
                          </>
                        ) : (
                          // <>{moment(notifyData.date).format("DD MMM, hh:mm A")}</>
                          <>{moment(notifyData.date).format("hh:mm A")}</>
                        )}
                      </p>
                    )}
                  </Col>
                </Row>
              </div>
            ))}
          </div>
        ))}
      </div>
    </div>
  );
}

export default NotificationDataRow;
