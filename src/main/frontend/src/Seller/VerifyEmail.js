import React from "react";
import { DefaultHeadBar, DefaultBodyPaper, DefaultFootBar } from "../Layout";
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
