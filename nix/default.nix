{ lib
, callPackage
, makeWrapper
, jdk11
}:

let
  buildGradle = callPackage ./gradle-env.nix { };
in

buildGradle rec {
  envSpec = ./gradle-env.json;
  pname = "squirrel_game";
  version = "1.0";

  gradleFlags = [ "desktop:dist" ];

  src = ../.;

  nativeBuildInputs = [ makeWrapper ];

  installPhase = ''
    mkdir -p $out/{lib,bin}
    cp desktop/build/libs/desktop-${version}.jar $out/lib/${pname}.jar


    makeWrapper ${jdk11}/bin/java $out/bin/${pname} \
      --add-flags "-jar $out/lib/${pname}.jar"
  '';

  meta = with lib; {
    description = "A 2D platformer video game with a squirrel";
    homepage = "https://github.com/chimado/squirrel_game";
    license = licenses.gpl3;
  };
}
