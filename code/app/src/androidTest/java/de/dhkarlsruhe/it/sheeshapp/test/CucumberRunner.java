package  de.dhkarlsruhe.it.sheeshapp.test;

import cucumber.api.CucumberOptions;

@CucumberOptions(features = "features",
        glue = {"de.dhkarlsruhe.it.sheeshapp.test;\n"},
        monochrome = true,
        plugin = {"pretty"}
)
public class CucumberRunner {
}
