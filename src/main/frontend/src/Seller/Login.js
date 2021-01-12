import React from "react";
import qs from "qs";
import HeadBar from "../Layout/HeadBar";
import BodyPaper from "../Layout/BodyPaper";
import FootBar from "../Layout/FootBar";
import LoginBody from "./LoginBody";

export default function Login({ location }) {
  const query = qs.parse(location.search, {
    ignoreQueryPrefix: true,
  });
  console.log(query.session);
  if (query.session === undefined) {
    return (
      <>
        <HeadBar />
        <BodyPaper InsideTag={<LoginBody />} />
        <FootBar />
      </>
    );
  } else {
    return <div>session: {query.session}</div>;
  }
}
