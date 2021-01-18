import React from "react";
import DefaultHeadBar from "../Layout/DefaultHeadBar";
import DefaultBodyPaper from "../Layout/DefaultBodyPaper";
import DefaultFootBar from "../Layout/DefaultFootBar";
import VerfiyEmailBody from "./VerfiyEmailBody";

export default function VerifyEmail({ location }) {
  return (
    <div>
      <DefaultHeadBar />
      <DefaultBodyPaper Body={<VerfiyEmailBody location={location} />} />
      <DefaultFootBar />
    </div>
  );
}
