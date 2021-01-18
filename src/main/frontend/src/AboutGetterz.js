import React from "react";
import DefaultHeadBar from "./Layout/DefaultHeadBar";
import DefaultBodyPaper from "./Layout/DefaultBodyPaper";
import DefaultFootBar from "./Layout/DefaultFootBar";
import AboutGetterzBody from "./AboutGetterzBody";

export default function AboutGetterz() {
  return (
    <div>
      <DefaultHeadBar />
      <DefaultBodyPaper Body={<AboutGetterzBody />} />
      <DefaultFootBar />
    </div>
  );
}
