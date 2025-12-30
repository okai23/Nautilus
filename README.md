# 🐚 Nautilus

**Nautilus** is a lightweight, custom-built Minecraft: Bedrock Edition server software written in Java.
Built from the ground up using **Netty** and **Cloudburst Protocol**, it aims to provide a high-performance, event-driven engine for Bedrock clients.

> **Status:** 🚧 **Pre-Alpha / Proof of Concept**
> *Current Protocol: Bedrock Edition 1.21.131 (Codec v898)*

---

## 🗺️ Project Roadmap

### Phase 1: The Foundation (Networking)
The core connection layer is stable. Clients can discover, connect, and complete the complex Bedrock handshake sequence.

- [x] **RakNet Implementation** (UDP listener via Cloudburst Netty)
- [x] **Protocol Codec Pipeline** (Packet encoding/decoding for v898)
- [x] **ZLIB Compression** (Handling 1.21+ compression thresholds)
- [x] **Login Sequence** (Client Data JWT decoding)
- [x] **Resource Pack Negotiation** (Accepting vanilla client stacks)
- [ ] **Resource Pack Transmission** (Feeding custom resource packs to clients)

### Phase 2: The Awakening (Spawning)
The "Locating Server" hurdle has been cleared. Players can successfully enter the game world.

- [x] **StartGame Packet** (Correct LevelSettings & Gamerules)
- [x] **Registry Loading** (Injecting valid `ItemDefinitions` & `BlockPalette`)
- [x] **Biome Definitions** (Correct NBT structure for `minecraft:plains`)
- [x] **Void Spawning** (Players spawn in dimension 2/The End to bypass chunks)
- [ ] **Xbox Live Authentication** (Validating the JWT signature against Mojang's Key)
- [ ] **Encryption** (ECDH Key Exchange & AES-256 Packet Encryption)

### Phase 3: The Body (Entity & Player Management)
Currently working on syncing player states so they aren't just floating cameras.

- [ ] **Player Sessions** (Wrapping `BedrockServerSession` in a `NautilusPlayer` object)
- [ ] **Skin Syncing** (`PlayerListPacket` with serialized skin data)
- [ ] **Entity Spawning** (`AddPlayerPacket` for seeing other players)
- [ ] **Movement Handling** (Processing `PlayerAuthInput` to prevent rubber-banding)
- [ ] **Command System** (Basic console inputs like `stop`, `kick`, `say`)

### Phase 4: The World (Terrain & Interaction)
The next major milestone is moving from a void world to a tangible one.

- [ ] **Chunk Logic** (Data palette storage & SubChunk formatting)
- [ ] **World Generation** (Basic flat world or void generator)
- [ ] **Block Breaking/Placing** (Handling `InventoryTransaction` packets)
- [ ] **Inventory System** (Container IDs & Window management)
- [ ] **Forms API** (Sending UI modals/buttons via `ModalFormRequest`)
