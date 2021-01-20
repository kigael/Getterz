import React from "react";
import DefaultHeadBar from "../Layout/DefaultHeadBar";
import DefaultBodyPaper from "../Layout/DefaultBodyPaper";
import DefaultFootBar from "../Layout/DefaultFootBar";
import VerifyEmailBody from "./VerifyEmailBody";

export default function VerifyEmail({ location }) {
  return (
    <div>
      <DefaultHeadBar />
      <DefaultBodyPaper Body={<VerifyEmailBody location={location} />} />
      <DefaultFootBar />
    </div>
  );
}
