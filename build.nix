{
  lib,
  stdenv,
  jdk21,
  maven
}:
let
  fs = lib.fileset;
  sourceFiles = ./.;
in

fs.trace sourceFiles

stdenv.mkDerivation {
  name = "demo";
  src =  fs.toSource {
    root = ./.;
    fileset = sourceFiles;
  };
  buildInputs = [ jdk21 maven];
  buildPhase = ''
      mvn -Dmaven.repo.local=$out clean install
  '';
}