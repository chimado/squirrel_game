{ lib
, self
, nix-filter
, callPackage
, makeWrapper
, jdk11
, libpulseaudio
}:

let
  buildGradle = callPackage ./gradle-env.nix { };
in

buildGradle rec {
  envSpec = ./gradle-env.json;
  pname = "squirrel-game";
  version = "1.0";

  src = nix-filter {
    root = self;
    include = [
      (nix-filter.inDirectory "core")
      (nix-filter.inDirectory "desktop")
      "build.gradle"
      "settings.gradle"
    ];
  };

  gradleFlags = [ "desktop:dist" ];

  nativeBuildInputs = [ makeWrapper ];

  installPhase = ''
    mkdir -p $out/{lib,bin}
    cp desktop/build/libs/desktop-${version}.jar $out/lib/${pname}.jar

    makeWrapper ${jdk11}/bin/java $out/bin/${pname} \
    --add-flags "-jar $out/lib/${pname}.jar" \
    --prefix LD_LIBRARY_PATH : ${lib.makeLibraryPath [ libpulseaudio ]}
  '';

  meta = with lib; {
    homepage = "https://github.com/chimado/squirrel-game";
    license = licenses.gpl3;
  };
}
