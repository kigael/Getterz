import React from "react";
import qs from "qs";
import DefaultHeadBar from "../Layout/DefaultHeadBar";
import DefaultBodyPaper from "../Layout/DefaultBodyPaper";
import DefaultFootBar from "../Layout/DefaultFootBar";
import LoginBody from "./LoginBody";

export default function Login({ location }) {
  const query = qs.parse(location.search, {
    ignoreQueryPrefix: true,
  });
  console.log(query.session);
  if (query.session === undefined) {
    return (
      <>
        <DefaultHeadBar />
        <DefaultBodyPaper InsideTag={<LoginBody />} />
        <DefaultFootBar />
      </>
    );
  } else {
    return <div>session: {query.session}</div>;
  }
}
