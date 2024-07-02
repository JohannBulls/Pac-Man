class Pacman extends React.Component {
    constructor(props) {
      super(props);
      this.state = { x: 0, y: 0 };
    }
  
    handleKeyDown = (event) => {
      switch(event.key) {
        case 'ArrowUp':
          this.setState({ y: this.state.y - 1 });
          break;
        case 'ArrowDown':
          this.setState({ y: this.state.y + 1 });
          break;
        case 'ArrowLeft':
          this.setState({ x: this.state.x - 1 });
          break;
        case 'ArrowRight':
          this.setState({ x: this.state.x + 1 });
          break;
        default:
          break;
      }
    }
  
    componentDidMount() {
      window.addEventListener('keydown', this.handleKeyDown);
    }
  
    componentWillUnmount() {
      window.removeEventListener('keydown', this.handleKeyDown);
    }
  
    render() {
      return (
        <div style={{ position: 'absolute', top: this.state.y, left: this.state.x }}>
          Pac-Man
        </div>
      );
    }
  }
  