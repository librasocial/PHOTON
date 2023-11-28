import React, { useState } from "react";
import { observer } from "mobx-react-lite";
import { Button, Dropdown, Form, Row, Col } from "react-bootstrap";
import "./styles.css";

const CheckboxMenu = React.forwardRef(
  (
    {
      children,
      style,
      className,
      "aria-labelledby": labeledBy,
      onSelectAll,
      onSelectNone,
    },
    ref
  ) => {
    return (
      <div
        ref={ref}
        style={style}
        className={`${className} CheckboxMenu`}
        aria-labelledby={labeledBy}
      >
        <div
          className="d-flex flex-column"
          style={{ maxHeight: "calc(245px)" }}
        >
          <ul
            className="list-unstyled flex-shrink mb-0"
            style={{ overflow: "auto" }}
          >
            {children}
          </ul>
          <Row className="border-top pb-0">
            <Col md={6}>
              <Button className="filterbtn btn btn-link" onClick={onSelectAll}>
                Select All
              </Button>
            </Col>
            <Col md={6} align="right">
              <Button className="filterbtn btn btn-link" onClick={onSelectNone}>
                Reset
              </Button>
            </Col>
          </Row>
        </div>
      </div>
    );
  }
);

const CheckDropdownItem = React.forwardRef(
  ({ children, id, checked, onChange }, ref) => {
    return (
      <Form.Group ref={ref} className="dropdown-item mb-0" controlId={id}>
        <Form.Check
          type="checkbox"
          label={children}
          checked={checked}
          onChange={onChange}
        />
      </Form.Group>
    );
  }
);

const SupplierName = observer((props) => {
  const [checked, setChecked] = useState([]);
  let items = props.items;
  let isChecked = (item) =>
    checked.includes(item) ? "checked-item" : "not-checked-item";

  const handleChecked = (e, label) => {
    if (e.target.checked == true) {
      items.find((i) => i.label === label).checked = e.target.checked;
      props.setSuplierName([...props.suplierName, label]);
    } else {
      items.find((i) => i.label === label).checked = e.target.checked;
      props.setSuplierName(props.suplierName.filter((id) => id !== label));
    }
  };

  const handleSelectAll = () => {
    items.map((i) => {
      props.suplierName.push(i.label);
    });
    items.forEach((i) => {
      i.checked = true;
    });
  };

  const handleSelectNone = () => {
    items.forEach((i) => {
      i.checked = false;
      props.setSuplierName([]);
    });
  };

  return (
    <Dropdown>
      <Dropdown.Toggle
        variant="primary"
        id="dropdown-basic"
        className="btn btn-primary dotEnBtn dotFilterBtn"
      >
        <i className="fa fa-filter inwards-filter" aria-hidden="true"></i>
      </Dropdown.Toggle>

      <Dropdown.Menu
        as={CheckboxMenu}
        onSelectAll={handleSelectAll}
        onSelectNone={handleSelectNone}
      >
        {items.map((i) => (
          <React.Fragment key={i.id}>
            <Dropdown.Item
              className={isChecked(i.label)}
              as={CheckDropdownItem}
              value={i.label || ""}
              id={i.id}
              checked={i.checked}
              onChange={(e) => handleChecked(e, i.label)}
            >
              <p id="content">
                {i.label.length > 28
                  ? `${i.label.substring(0, 28)}...`
                  : i.label}
              </p>
              <p id="content">
                {i.add.length > 20 ? `${i.add.substring(0, 20)}...` : i.add}
              </p>
            </Dropdown.Item>
          </React.Fragment>
        ))}
      </Dropdown.Menu>
    </Dropdown>
  );
});

export default SupplierName;
