import React, { useCallback } from "react";
import { useSelector } from "react-redux";
import { useHistory } from "react-router-dom";
import HeadBar from "../Layout/HeadBar";
import BodyPaper from "../Layout/BodyPaper";
import FootBar from "../Layout/FootBar";
import SignupBody from "./SignupBody";

export default function Signup() {
  const { header, loading } = useSelector((state) => ({
    header: state.BuyerInfo.header,
    loading: state.BuyerInfo.loading,
  }));
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/buyer"), [history]);
  if (!loading && header.session) {
    redirecTo();
  }
  return (
    <div>
      <HeadBar />
      <BodyPaper InsideTag={<SignupBody />} />
      <FootBar />
    </div>
  );
}
