{
  description = "yet another riscv cpu";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs";
  };

  outputs = inputs @ {flake-parts, ...}:
    flake-parts.lib.mkFlake {inherit inputs;} {
      imports = [
        # To import a flake module
        # 1. Add foo to inputs
        # 2. Add foo as a parameter to the outputs function
        # 3. Add here: foo.flakeModule
      ];
      systems = ["x86_64-linux" "aarch64-linux"];
      perSystem = {
        config,
        self',
        inputs',
        pkgs,
        system,
        ...
      }: {
        packages = {};
        devShells.default = pkgs.mkShell {
          nativeBuildInputs = with pkgs; [
            ammonite
            coursier
            jdk_headless
            mill
            sbt
            scala-cli
            scalafmt
            circt
            yosys
            nextpnrWithGui
            trellis
            cargo rustc
          ];
        };
        checks = {};
      };
      flake = {
        hydraJobs = {
          inherit (inputs.self) packages devShells checks;
        };
      };
    };
}
