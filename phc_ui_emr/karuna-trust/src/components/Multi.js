import React, { useState, useEffect, useRef } from "react";
import Select from "@khanacademy/react-multi-select";

function Multi(props) {
  const [isLoading, setIsLoading] = useState(false);
  const [selected, setSelected] = useState("");

  const options = [
    { label: "KPI Degradtion", value: "kpi_degradation" },
    { label: "Sleeping Cell", value: "sleeping_cell" },
    { label: "Anomaly", value: "anomaly" },
    { label: "Label1", value: "label_1" },
    { label: "Label2fgfgfgfghfghgh", value: "label_2" },
    { label: "Label3", value: "label_3" },
    { label: "Label4", value: "label_4" },
    { label: "Label5", value: "label_5" },
  ];

  const handleSelectedChanged = (value) => {
    // props.setItems(value)
    setSelected(value);
  };

  useEffect(() => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
    }, 1000);
  }, [setIsLoading]);

  return (
    <Select
      options={options}
      onSelectedChanged={handleSelectedChanged}
      selected={selected}
      isLoading={isLoading}
      disabled={isLoading}
      disableSearch={false}
      renderHeader={false}
      overrideStrings={{
        selectSomeItems: "do me a favor by selecting something",
        //allItemsAreSelected: "You have gone nuts... all selected",
        //selectAll: "do u wanna select all of them?",
        search: "Fantasy search",
      }}
    />
  );
}

export default Multi;
