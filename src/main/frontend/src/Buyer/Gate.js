import React, { useCallback } from "react";
import { useHistory } from "react-router-dom";
import { useSelector } from "react-redux";
import HeadBar from "../Layout/HeadBar";
import BodyPaper from "../Layout/BodyPaper";
import FootBar from "../Layout/FootBar";
import GateBody from "./GateBody";

export default function Gate() {
  const { header, loading } = useSelector((state) => ({
    header: state.BuyerInfo.header,
    loading: state.BuyerInfo.loading,
  }));
  const history = useHistory();
  const redirecTo = useCallback(() => history.push("/buyer/login"), [history]);
  if (!loading && !header.session) {
    redirecTo();
  }
  return (
    <div>
      <HeadBar Type="Buyer" />
      <BodyPaper Type="Buyer" InsideTag={<GateBody />} />
      <FootBar Type="Buyer" />
    </div>
  );
}
