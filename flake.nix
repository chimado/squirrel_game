{
  description = "A very minimal flake for building and running Squirrel Game";

  inputs.nixpkgs.url = "github:nixos/nixpkgs/nixpkgs-unstable";
  inputs.nix-filter.url = "github:numtide/nix-filter";

  outputs = { self, nixpkgs, nix-filter }:
    let
      supportedSystems = [ "x86_64-linux" "x86_64-darwin" "aarch64-linux" "aarch64-darwin" ];
      forAllSystems = nixpkgs.lib.genAttrs supportedSystems;
      nixpkgsFor = forAllSystems (system: nixpkgs.legacyPackages.${system});
    in
    {
      packages = forAllSystems (system: rec {
        squirrel-game = nixpkgsFor.${system}.callPackage ./nix/default.nix { nix-filter = nix-filter.lib; inherit self; };
        default = squirrel-game;
      });

      apps = forAllSystems (system: rec {
        squirrel-game = { type = "app"; program = "${self.packages.${system}.squirrel-game}/bin/squirrel-game"; };
        default = squirrel-game;
      });
    };
}
