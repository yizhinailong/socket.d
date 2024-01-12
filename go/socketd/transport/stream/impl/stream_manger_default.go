package impl

import (
	"fmt"

	"socketd/transport/core"
	"socketd/transport/stream"
)

var _ stream.StreamManager = new(StreamManagerDefault)

type StreamManagerDefault struct {
	config    core.Config
	streamMap map[string]stream.StreamInternal
}

func NewStreamManagerDefault(config core.Config) *StreamManagerDefault {
	return &StreamManagerDefault{
		config:    config,
		streamMap: make(map[string]stream.StreamInternal),
	}
}

func (s StreamManagerDefault) Add(sid string, stream stream.StreamInternal) {
	s.streamMap[sid] = stream
}

func (s StreamManagerDefault) Get(sid string) stream.StreamInternal {
	stm, _ := s.streamMap[sid]
	return stm
}

func (s StreamManagerDefault) Remove(sid string) {
	stm, ok := s.streamMap[sid]
	if ok {
		stm.InsuranceCancel()
		// TODO 日志记录
		fmt.Printf("%s stream removed, sid = %s\n", s.config.GetRoleName(), sid)
	}
	delete(s.streamMap, sid)
}
