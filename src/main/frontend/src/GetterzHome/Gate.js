import React from "react";
import { DefaultHeadBar, DefaultBodyPaper, DefaultFootBar } from "../Layout";
import GateBody from "./GateBody";

export default function Gate() {
  return (
    <>
      <DefaultHeadBar />
      <DefaultBodyPaper Body={<GateBody />} />
      <DefaultFootBar />
    </>
  );
}
