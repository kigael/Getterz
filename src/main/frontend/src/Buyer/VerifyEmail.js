import React from "react";
import HeadBar from "../Layout/HeadBar";
import BodyPaper from "../Layout/BodyPaper";
import FootBar from "../Layout/FootBar";
import VerfiyEmailBody from "./VerfiyEmailBody";

export default function VerifyEmail({ location }) {
  return (
    <div>
      <HeadBar />
      <BodyPaper InsideTag={<VerfiyEmailBody location={location} />} />
      <FootBar />
    </div>
  );
}
