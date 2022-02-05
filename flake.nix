{
  description = "A very minimal flake for building and running squirrel_game";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      {
        packages.squirrel_game = pkgs.callPackage ./nix/default.nix { };
        defaultPackage = self.packages.${system}.squirrel_game;

        apps.squirrel_game = flake-utils.lib.mkApp { drv = self.defaultPackage.${system}; name = "squirrel_game"; };
        defaultApp = self.apps.${system}.squirrel_game;
      }
    );
}
