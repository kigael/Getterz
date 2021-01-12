import React from "react";
import HeadBar from "./Layout/HeadBar";
import BodyPaper from "./Layout/BodyPaper";
import FootBar from "./Layout/FootBar";
import AboutGetterzBody from "./AboutGetterzBody";

export default function AboutGetterz() {
  return (
    <div>
      <HeadBar />
      <BodyPaper InsideTag={<AboutGetterzBody />} />
      <FootBar />
    </div>
  );
}
