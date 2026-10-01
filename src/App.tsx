import { useEffect, useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import WatchViewport from "./components/WatchViewport";
import BPMSimulatorPanel from "./components/BPMSimulatorPanel";
import PanicButtonScreen from "./components/PanicButtonScreen";
import BreathingGuideScreen from "./components/BreathingGuideScreen";
import { AUTO_TRIGGER_BPM, getSeverity, STABLE_BPM, type Screen } from "./types";

export default function App() {
  const [bpm, setBpm] = useState(72);
  const [screen, setScreen] = useState<Screen>("panic");
  const [dismissed, setDismissed] = useState(false); // evita reabrir tras salir manualmente
  const severity = getSeverity(bpm);

  // Disparo automático: BPM > 100 activa el Modo Crisis
  useEffect(() => {
    if (bpm > AUTO_TRIGGER_BPM && screen === "panic" && !dismissed) setScreen("breathing");
    if (bpm <= AUTO_TRIGGER_BPM) setDismissed(false);
    
    // Retorno automático: si los latidos se estabilizan, salir del modo respiración
    if (bpm <= STABLE_BPM && screen === "breathing") {
      setScreen("panic");
    }
  }, [bpm, screen, dismissed]);

  const exit = () => { setDismissed(true); setScreen("panic"); };

  return (
    <main className="flex min-h-screen flex-col items-center justify-center gap-10 bg-slate-950 p-6 lg:flex-row">
      <div className="text-center">
        <h1 className="mb-6 text-2xl font-semibold text-slate-100">CalmWrist</h1>
        <WatchViewport haptic={screen === "breathing"} severity={severity}>
          <AnimatePresence mode="wait">
            <motion.div key={screen} className="h-full w-full" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }} transition={{ duration: 0.3 }}>
              {screen === "panic"
                ? <PanicButtonScreen bpm={bpm} severity={severity} onTrigger={() => setScreen("breathing")} />
                : <BreathingGuideScreen bpm={bpm} severity={severity} onExit={exit} />}
            </motion.div>
          </AnimatePresence>
        </WatchViewport>
      </div>
      <BPMSimulatorPanel bpm={bpm} onChange={setBpm} />
    </main>
  );
}
