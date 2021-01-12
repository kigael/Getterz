import React from "react";
import HeadBar from "./Layout/HeadBar";
import BodyPaper from "./Layout/BodyPaper";
import FootBar from "./Layout/FootBar";
import GateBody from "./GateBody";

export default function Gate() {
  return (
    <div>
      <HeadBar />
      <BodyPaper InsideTag={<GateBody />} />
      <FootBar />
    </div>
  );
}
