import React from "react";
import { DefaultHeadBar, DefaultBodyPaper, DefaultFootBar } from "../Layout";
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
