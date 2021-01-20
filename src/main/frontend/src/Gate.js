import React from "react";
import DefaultHeadBar from "./Layout/DefaultHeadBar";
import DefaultBodyPaper from "./Layout/DefaultBodyPaper";
import DefaultFootBar from "./Layout/DefaultFootBar";
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
