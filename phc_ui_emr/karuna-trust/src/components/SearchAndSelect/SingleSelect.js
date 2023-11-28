import React, { useState } from "react";
import "./drop.css";

function SingleSelect(props) {
  const [visibility, setVisibility] = useState(false);
  const [selectedOption, setSelectedOption] = useState("");
  const [search, setSearch] = useState("");
  const options = [
    "Atenlol 25mg",
    "Ascrobic Acid",
    "Cefuxime 100mg",
    "Hipress 50mg",
  ];

  return (
    <div>
      <div
        className="select"
        onClick={(e) => {
          setVisibility(!visibility);
          setSearch("");
          // e.currentTarget.children[0].children[1].innerHTML = visibility
          //     ? "arrow_drop_down"
          //     : "arrow_drop_up";
        }}
      >
        <div className="selected-option">
          <span
            title={selectedOption === "" ? "Select a Medicine" : selectedOption}
          >
            {selectedOption === ""
              ? "Select a Medicine"
              : selectedOption.length <= 20
              ? selectedOption
              : `${selectedOption.slice(0, 20)}...`}
          </span>
        </div>
        {visibility && (
          <div className="options">
            <div className="search-options">
              <input
                type="text"
                placeholder="Search Medicine"
                defaultValue={search}
                onClick={(e) => e.stopPropagation()}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
            <ul>
              <li
                data-value="default"
                name="name"
                onClick={(e) => props.handleInputChange(e)}
                hidden
              >
                Select a Medicine
              </li>
              {options
                .filter((option) =>
                  option.toLowerCase().includes(search.toLowerCase())
                )
                .map((option, index) => (
                  <li
                    key={index}
                    className={
                      selectedOption === option ? "active-option" : null
                    }
                    value={props.medicine}
                    name="name"
                    onClick={(e) => props.handleInputChange(option, i)}
                  >
                    {option}
                  </li>
                ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}

export default SingleSelect;
