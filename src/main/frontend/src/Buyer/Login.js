import React, { useCallback } from "react";
import { useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import HeadBar from "../Layout/HeadBar";
import BodyPaper from "../Layout/BodyPaper";
import FootBar from "../Layout/FootBar";
import LoginBody from "./LoginBody";

export default function Login() {
  const header = useSelector((state) => state.BuyerInfo.header);
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/buyer"), [history]);
  console.log(header.session);
  if (header.session) {
    redirecTo();
  }
  return (
    <>
      <HeadBar />
      <BodyPaper InsideTag={<LoginBody />} />
      <FootBar />
    </>
  );
}
