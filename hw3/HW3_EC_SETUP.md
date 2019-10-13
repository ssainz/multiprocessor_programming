# Extra Credit Setup Instructions

## Requirements

The Clojure compiler required for running the Knosses Linearizability Checker seems to work only with JDK8 or JDK9 at the moment. If you have any other versions, you need to install either JDK8 or 9 to complete this task

## Clone/Download Github Repository

The Knosses linearizability checker is hosted at https://github.com/jepsen-io/knossos.

`git clone https://github.com/jepsen-io/knossos`

Alternatively, you can download the zip file from the URL above and extract it to an appropriate location

## Install Leiningen

The instructions can be found at https://leiningen.org/.

You can download the appropriate lein script file ([Linux/Mac](https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein), [Windows](https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein.bat)) and save it into the knossos directory you cloned/downloaded in the previous step.

## Building and Running Knosses

Execute the following commands from the cloned repository or the extracted folder.

### Building

The following command will build the checker:

`./lein install`

### Running

The instruction to run the checker can be found at https://github.com/jepsen-io/knossos#as-a-library.

All the models required for this assignment are available at https://github.com/jepsen-io/knossos/blob/master/src/knossos/model.clj. You just need to use the appropriate model name when running the analyzer.